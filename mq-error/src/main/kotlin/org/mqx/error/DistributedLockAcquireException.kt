package org.mqx.error

class DistributedLockAcquireException(throwable: Throwable) : RetryableException(throwable)