package org.mqx.context.ctx

import com.mqx.mq4kt.core.annotations.QueueEvent
import org.mqx.context.annotations.Worker
import org.mqx.context.impl.EventConsumer
import org.mqx.context.impl.QueueEventListener
import org.mqx.error.MQueueEventListenerClassNotValidException
import org.mqx.error.WorkerClassNotValidException
import org.springframework.beans.factory.BeanNameAware
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
open class MqContext : BeanNameAware, InitializingBean {
    private var name: String? = null

    private val workers = HashMap<String, EventConsumer<*, *, *>>()
    private val queueListeners = HashMap<String, QueueEventListener<*, *>>()

    private lateinit var applicationContext: ApplicationContext

    open fun initContext(context: ApplicationContext) {
        applicationContext = context

        initWorkerBeans(context)
        initQueueEventListenerBeans(context)
    }

    private fun initWorkerBeans(ctx: ApplicationContext) {
        val workerBeans = ctx.getBeansWithAnnotation(Worker::class.java)
        workerBeans.entries.forEach { worker ->
            if (worker.value is EventConsumer<*, *, *>) {
                val workerClz = worker.value as EventConsumer<*, *, *>

                val workerAnnotation = ctx.findAnnotationOnBean(worker.key, Worker::class.java) ?: return@forEach

                if (workers.containsKey(workerAnnotation.queueName)) {
                    throw WorkerClassNotValidException("Duplicate Worker class with name: ${worker.key}. Worker classes' name should be unique")
                }
                workers[workerAnnotation.queueName] = workerClz
            } else {
                throw WorkerClassNotValidException("Worker class with name: ${worker.key} has to implements EventConsumer class of the library")
            }
        }
    }

    private fun initQueueEventListenerBeans(ctx: ApplicationContext) {
        val queueEventListenerBeans = ctx.getBeansWithAnnotation(QueueEvent::class.java)

        queueEventListenerBeans.entries.forEach { queueEventListener ->
            if (queueEventListener.value is QueueEventListener<*, *>) {
                val listenerClz = queueEventListener.value as QueueEventListener<*, *>

                val queueListenerEventAnnotation =
                    ctx.findAnnotationOnBean(queueEventListener.key, QueueEvent::class.java)

                if (queueListeners.containsKey(queueListenerEventAnnotation.queueName)) {
                    throw MQueueEventListenerClassNotValidException("Duplicate MQueue event listeners class with name: ${queueEventListener.key}. Listener classes' name should be unique")
                }
                queueListeners[queueListenerEventAnnotation.queueName] = listenerClz
            } else {
                throw MQueueEventListenerClassNotValidException("MQueue event listeners class with name: ${queueEventListener.key} has to implements QueueEventListener class of the library")
            }
        }
    }

    open fun getWorker(queueName: String): EventConsumer<*, *, *>? {
        return workers[queueName]
    }

    open fun getQueueEventListener(queueName: String): QueueEventListener<*, *>? {
        return queueListeners[queueName]
    }

    override fun setBeanName(name: String) {
        this.name = name
    }

    override fun afterPropertiesSet() {

    }
}