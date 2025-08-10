package org.mqx.context.ctx

import org.mqx.context.annotations.QueueEvent
import org.mqx.context.annotations.Worker
import org.mqx.context.consumer.WorkerProcessor
import org.mqx.context.job.Job
import org.mqx.context.job.JobOptions
import org.mqx.context.queue.MQueue
import org.mqx.context.queue.QueueEventListener
import org.mqx.error.MQueueEventListenerClassNotValidException
import org.mqx.error.WorkerClassNotValidException
import org.springframework.context.ApplicationContext
import java.util.concurrent.ConcurrentHashMap

object MqxContext {
    private var initialized = false
    private val workers = ConcurrentHashMap<String, WorkerProcessor<*, *>>()
    private val queues = ConcurrentHashMap<String, MQueue<*>>()
    private val queueListeners = ConcurrentHashMap<String, QueueEventListener<*, *, *>>()

    private lateinit var applicationContext: ApplicationContext

    fun initContext(context: ApplicationContext) {
        synchronized(this) {
            if (initialized) {
                throw IllegalStateException("MqxContext has been initialized")
            }
            initialized = true
            applicationContext = context

            initWorkerBeans(context)
            initQueueEventListenerBeans(context)
        }
    }

    private fun initWorkerBeans(ctx: ApplicationContext) {
        val workerBeans = ctx.getBeansWithAnnotation(Worker::class.java)
        workerBeans.entries.forEach { (key, processor) ->
            if (processor !is WorkerProcessor<*, *>) {
                throw WorkerClassNotValidException(
                    """
                        Worker class with name: $key has to implements EventConsumer class of the library
                    """.trimIndent()
                )
            }

            val workerAnnotation = ctx.findAnnotationOnBean(
                key,
                Worker::class.java
            ) ?: return@forEach
            ctx.getBean(key, WorkerProcessor::class.java)

            if (workers.containsKey(workerAnnotation.queueName)) {
                throw WorkerClassNotValidException(
                    """
                    Duplicate Worker class with name: ${key}. Worker classes' name has to be unique
                """.trimIndent()
                )
            }
            workers[workerAnnotation.queueName] = processor
            injectQueues(workerProcessor = processor, workerAnnotation = workerAnnotation)
        }
    }

    private fun injectQueues(
        workerProcessor: WorkerProcessor<*, *>,
        workerAnnotation: Worker
    ) {
        queues[workerAnnotation.queueName] = object : MQueue<Any> {
            override fun add(job: Any, options: JobOptions) {
                TODO("Not yet implemented")
            }

            override fun getJobs(): Array<Job<Any, *, *>> {
                TODO("Not yet implemented")
            }

            override fun drain() {
                TODO("Not yet implemented")
            }

            override fun clean() {
                TODO("Not yet implemented")
            }

            override fun pause() {
                TODO("Not yet implemented")
            }

            override fun resume() {
                TODO("Not yet implemented")
            }

            override fun isPaused() {
                TODO("Not yet implemented")
            }

            override fun removeIfExists(id: String): Job<Any, *, *>? {
                TODO("Not yet implemented")
            }
        }
    }

    private fun initQueueEventListenerBeans(ctx: ApplicationContext) {
        val queueEventListenerBeans = ctx.getBeansWithAnnotation(QueueEvent::class.java)

        queueEventListenerBeans.entries.forEach { queueEventListener ->
            if (queueEventListener.value !is QueueEventListener<*, *, *>) {
                throw MQueueEventListenerClassNotValidException(
                    """
                        MQueue event listeners class with name: ${queueEventListener.key} has to implements QueueEventListener class of the library
                    """.trimIndent()
                )
            }

            val listenerClz = queueEventListener.value as QueueEventListener<*, *, *>
            val queueListenerEventAnnotation =
                ctx.findAnnotationOnBean(
                    queueEventListener.key,
                    QueueEvent::class.java
                ) ?: return@forEach

            if (queueListeners.containsKey(queueListenerEventAnnotation.queueName)) {
                throw MQueueEventListenerClassNotValidException(
                    """
                        Duplicate MQueue event listeners class with name: ${queueEventListener.key}. Listener classes' name has to be unique
                    """.trimIndent()
                )
            }
            queueListeners[queueListenerEventAnnotation.queueName] = listenerClz
        }
    }
}