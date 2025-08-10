package org.mqx.context.job

interface Job<D, R, E : Throwable> {
    fun getId(): String

    fun getData(): D

    fun getResult(): R?

    fun getError(): E?

    fun getPriority(): Int

    fun isInProgress(): Boolean

    fun isFinished(): Boolean

    fun isFailed(): Boolean

    fun isWaiting(): Boolean

    fun isExpired(): Boolean

    fun isRetrying(): Boolean

    fun getOptions(): JobOptions
}