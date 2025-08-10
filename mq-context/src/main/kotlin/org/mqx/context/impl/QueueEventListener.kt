package org.mqx.context.impl

interface QueueEventListener<T, E : MQXueueEvent<T>> {
    fun listen(event: E)
}