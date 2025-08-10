package com.mqx.mq4kt.core.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueueEvent(val queueName: String)
