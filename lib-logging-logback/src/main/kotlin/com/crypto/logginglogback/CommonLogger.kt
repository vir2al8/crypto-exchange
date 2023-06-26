package com.crypto.logginglogback

import ch.qos.logback.classic.Logger
import com.crypto.loggingcommon.CommonLogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun commonLoggerLogback(logger: Logger): CommonLogWrapper = CommonLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun commonLoggerLogback(clazz: KClass<*>): CommonLogWrapper = commonLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun commonLoggerLogback(loggerId: String): CommonLogWrapper = commonLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
