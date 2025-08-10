package org.mqx.core.commands

import org.mqx.error.DistributedLockAcquireException
import org.mqx.util.mq4KtLogger
import org.redisson.api.RedissonClient

fun RedissonClient.executeOnLock(key: String, fn: () -> Unit) {
    val log = mq4KtLogger()

    val lock = getLock(key)
    try {
        try {
            lock.lock()
        } catch (ex: Exception) {
            log.error("Failed to acquire distributed-lock due to ${ex.message}", ex)
            throw DistributedLockAcquireException(ex)
        }
        return fn()
    } finally {
        try {
            lock.unlock()
        } catch (illegalMonitoringEx: IllegalMonitorStateException) {
            log.error(
                "Failed to unlock due to illegal monitoring exception ${illegalMonitoringEx.message}",
                illegalMonitoringEx
            )
            throw illegalMonitoringEx
        } catch (ex: Exception) {
            log.error(
                "Failed to unlock due to exception ${ex.message}", ex
            )
            throw ex
        }
    }
}