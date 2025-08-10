package org.mqx.core.commands

import org.redisson.api.RedissonClient

fun RedissonClient.incrementRetryCommand(key: String): Long {
    return this.getAtomicLong("retry:job:$key").incrementAndGet()
}
