package org.mqx.context.impl

import org.springframework.context.ApplicationEvent

abstract class MQXueueEvent<T>(source: Any) : ApplicationEvent(source)