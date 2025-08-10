package org.mqx.context.impl

interface EventConsumer<T, D, R> {
    fun consume(data: T, job: Job<D, R>): R
}