package org.mqx.context.queue

import org.mqx.context.job.Job
import org.mqx.context.job.JobOptions
import java.io.Serializable

interface MQueue<T : Serializable> {
    fun add(job: T, options: JobOptions = JobOptions.default())
    fun getJobs(): Array<Job<T, *, *>>
    fun drain()
    fun clean()
    fun pause()
    fun resume()
    fun isPaused()
    fun removeIfExists(id: String): Job<T, *, *>?
}