package org.mqx.context.queue

interface QueueEventListener<D, R, E : Throwable> {
    fun active(event: MQXQueueEvent<D, R, E>)
    fun added(event: MQXQueueEvent<D, R, E>)
    fun cleaned(event: MQXQueueEvent<D, R, E>)
    fun completed(event: MQXQueueEvent<D, R, E>)
    fun delayed(event: MQXQueueEvent<D, R, E>)
    fun drained(event: MQXQueueEvent<D, R, E>)
    fun duplicated(event: MQXQueueEvent<D, R, E>)
    fun error(event: MQXQueueEvent<D, R, E>)
    fun failed(event: MQXQueueEvent<D, R, E>)
    fun paused()
    fun removed(event: MQXQueueEvent<D, R, E>)
}