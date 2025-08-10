package org.mqx.context.impl

interface Job<D, R> {
    fun getId(): String

    fun getPayload(): D

    fun getPriority(): Int

    fun isInProgress(): Boolean

    fun isFinished(): Boolean

    fun isFailed(): Boolean

    fun isWaiting(): Boolean

    fun isExpired(): Boolean

    fun isRetrying(): Boolean
}