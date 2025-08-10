package org.mqx.context.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.TypedJsonJacksonCodec
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
@AutoConfiguration
open class MqxAutoConfiguration(
    private val mqxRedisProperties: RedisProperties
) {

    @Bean("mqxRedisBean")
    open fun redissonClient(): RedissonClient {
        return Redisson.create(Config().apply {
            useMasterSlaveServers().masterAddress = "redis://${mqxRedisProperties.host}:${mqxRedisProperties.port}"
            useMasterSlaveServers().database = mqxRedisProperties.database
            codec = TypedJsonJacksonCodec(Any::class.java)
        })
    }

    @Bean("mqxWorkerPool")
    open fun mqxWorkerPool(): ExecutorService = Executors.newThreadPerTaskExecutor(
        Thread.ofVirtual().name("MQ-X-Worker-Thread-%d").factory()
    )
}