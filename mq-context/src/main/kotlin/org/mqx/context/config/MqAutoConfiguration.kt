package org.mqx.context.config

import org.mqx.context.properties.MqxRedisProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

@AutoConfiguration
@EnableConfigurationProperties(value = [MqxRedisProperties::class])
open class MqAutoConfiguration