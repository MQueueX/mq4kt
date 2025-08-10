package org.mqx.error

open class Mq4ktException(override val message: String?, override val cause: Throwable?) :
    RuntimeException(message) {
    constructor() : this(null, null)
    constructor(message: String) : this(message, null)
    constructor(throwable: Throwable) : this(throwable.message, throwable)
}