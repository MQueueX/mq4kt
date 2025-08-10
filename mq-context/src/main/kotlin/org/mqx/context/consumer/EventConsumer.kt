package org.mqx.context.consumer

import org.mqx.context.job.Job

interface EventConsumer<T, D, R> {
    fun consume(job: Job<D, R, *>): R
}