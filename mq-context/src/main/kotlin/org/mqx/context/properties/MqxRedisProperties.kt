package org.mqx.context.properties

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MqxRedisProperties {
    @Bean
    @ConfigurationProperties(prefix = "mqx.redis")
    open fun mqxRedisProperties(): RedisProperties {
        return RedisProperties()
    }
}