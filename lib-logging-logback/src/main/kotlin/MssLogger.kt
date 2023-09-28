package ru.mss.lib.logging.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.mss.lib.logging.common.IMssLogWrapper
import kotlin.reflect.KClass

/**
 * Generate internal MssLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mssLoggerLogback(logger: Logger): IMssLogWrapper = MssLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun mssLoggerLogback(clazz: KClass<*>): IMssLogWrapper = mssLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun mssLoggerLogback(loggerId: String): IMssLogWrapper = mssLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)