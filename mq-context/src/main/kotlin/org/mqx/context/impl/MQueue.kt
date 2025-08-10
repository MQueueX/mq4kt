package org.mqx.context.impl
import java.io.Serializable

interface MQueue<T : Serializable> {
    fun add(job: Job<T, *>)
    fun addBulk(jobs: Array<Job<T, *>>)
    fun getJobs(): Array<Job<T, *>>
    fun drain()
    fun clean()
    fun pause()
    fun resume()
    fun isPaused()
    fun removeIfExists(id: String): Job<T, *>
}