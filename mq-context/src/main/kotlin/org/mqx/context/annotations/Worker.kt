package org.mqx.context.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Worker(val queueName: String, val importance: Int)
