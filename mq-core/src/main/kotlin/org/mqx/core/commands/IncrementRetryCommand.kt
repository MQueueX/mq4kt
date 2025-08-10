package com.mqx.mq4kt.commands

import org.redisson.api.RedissonClient

fun RedissonClient.incrementRetryCommand(key: String): Long {
    return this.getAtomicLong("retry:job:$key").incrementAndGet()
}
