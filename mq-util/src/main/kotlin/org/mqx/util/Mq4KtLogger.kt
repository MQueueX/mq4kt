package org.mqx.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.mq4KtLogger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}