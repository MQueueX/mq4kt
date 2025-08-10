package org.mqx.context.consumer

import org.mqx.context.job.Job

interface WorkerProcessor<D, R> {
    fun process(job: Job<D, R, *>): R
}