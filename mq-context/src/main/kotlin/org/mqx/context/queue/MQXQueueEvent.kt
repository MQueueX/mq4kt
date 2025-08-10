package org.mqx.context.queue

import org.mqx.context.job.Job
import org.springframework.context.ApplicationEvent

abstract class MQXQueueEvent<D, R, E : Throwable>(data: Job<D, R, E>, source: Any) : ApplicationEvent(source)