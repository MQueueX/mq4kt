package org.mqx.context.ctx

import org.mqx.context.annotations.QueueEvent
import org.mqx.context.annotations.Worker
import org.mqx.context.consumer.EventConsumer
import org.mqx.context.queue.QueueEventListener
import org.mqx.error.MQueueEventListenerClassNotValidException
import org.mqx.error.WorkerClassNotValidException
import org.springframework.context.ApplicationContext
import java.util.concurrent.ConcurrentHashMap

object MqxContext {
    private var initialized = false
    private val workers = ConcurrentHashMap<String, EventConsumer<*, *, *>>()
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
        workerBeans.entries.forEach { worker ->
            if (worker.value !is EventConsumer<*, *, *>) {
                throw WorkerClassNotValidException("Worker class with name: ${worker.key} has to implements EventConsumer class of the library")
            }

            val workerClz = worker.value as EventConsumer<*, *, *>
            val workerAnnotation = ctx.findAnnotationOnBean(worker.key, Worker::class.java) ?: return@forEach

            if (workers.containsKey(workerAnnotation.queueName)) {
                throw WorkerClassNotValidException("Duplicate Worker class with name: ${worker.key}. Worker classes' name has to be unique")
            }
            workers[workerAnnotation.queueName] = workerClz
        }
    }

    private fun initQueueEventListenerBeans(ctx: ApplicationContext) {
        val queueEventListenerBeans = ctx.getBeansWithAnnotation(QueueEvent::class.java)

        queueEventListenerBeans.entries.forEach { queueEventListener ->
            if (queueEventListener.value !is QueueEventListener<*, *, *>) {
                throw MQueueEventListenerClassNotValidException("MQueue event listeners class with name: ${queueEventListener.key} has to implements QueueEventListener class of the library")
            }

            val listenerClz = queueEventListener.value as QueueEventListener<*, *, *>
            val queueListenerEventAnnotation =
                ctx.findAnnotationOnBean(queueEventListener.key, QueueEvent::class.java) ?: return@forEach

            if (queueListeners.containsKey(queueListenerEventAnnotation.queueName)) {
                throw MQueueEventListenerClassNotValidException("Duplicate MQueue event listeners class with name: ${queueEventListener.key}. Listener classes' name has to be unique")
            }
            queueListeners[queueListenerEventAnnotation.queueName] = listenerClz
        }
    }
}