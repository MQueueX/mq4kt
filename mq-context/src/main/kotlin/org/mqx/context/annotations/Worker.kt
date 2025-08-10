package org.mqx.context.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Worker(val queueName: String, val dataType: KClass<*>, val importance: Int)
