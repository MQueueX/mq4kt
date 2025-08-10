package org.mqx.context.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueueEvent(val queueName: String)
