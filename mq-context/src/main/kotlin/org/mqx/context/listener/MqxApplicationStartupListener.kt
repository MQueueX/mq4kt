package org.mqx.context.listener

import org.mqx.context.ctx.MqxContext
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent

open class MqxApplicationStartupListener(
    private val ctx: ApplicationContext,
) : ApplicationListener<ContextRefreshedEvent> {

    override fun onApplicationEvent(contextRefreshedEvent: ContextRefreshedEvent) {
        MqxContext.initContext(ctx)
    }
}