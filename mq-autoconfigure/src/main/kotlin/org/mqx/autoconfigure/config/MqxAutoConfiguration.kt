package org.mqx.autoconfigure.config

import org.mqx.context.listener.MqxApplicationStartupListener
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.TypedJsonJacksonCodec
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
@AutoConfiguration
open class MqxAutoConfiguration() {

    @Bean
    @ConfigurationProperties(prefix = "mqx.redis")
    open fun mqxRedisProperties(): RedisProperties {
        return RedisProperties()
    }

    @Bean("mqxRedisBean")
    @ConditionalOnMissingBean
    @DependsOn("mqxRedisProperties")
    open fun redissonClient(
        mqxRedisProperties: RedisProperties
    ): RedissonClient {
        return Redisson.create(Config().apply {
            useMasterSlaveServers().masterAddress = "redis://${mqxRedisProperties.host}:${mqxRedisProperties.port}"
            useMasterSlaveServers().database = mqxRedisProperties.database
            codec = TypedJsonJacksonCodec(Any::class.java)
        })
    }

    @Bean("mqxWorkerPool")
    @ConditionalOnMissingBean
    open fun mqxWorkerPool(): ExecutorService = Executors.newThreadPerTaskExecutor(
        Thread.ofVirtual().name("MQ-X-Worker-Thread-%d").factory()
    )

    @Bean
    open fun mqxApplicationStartupListener(ctx: ApplicationContext): MqxApplicationStartupListener {
        return MqxApplicationStartupListener(ctx)
    }
}