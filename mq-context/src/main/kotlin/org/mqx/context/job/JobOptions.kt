package org.mqx.context.job

data class JobOptions(
    val priority: Int = 0,
    val delay: Long = 0L,
    val attempts: Int = 1,
    val lifo: Boolean = false,
    val removeOnComplete: Boolean = false,
    val removeOnFail: Boolean = false,
    val jobId: String? = null
) {
    companion object {
        fun default() = JobOptions()
    }
}
