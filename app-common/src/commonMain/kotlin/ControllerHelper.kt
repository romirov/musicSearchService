package ru.mss.app.common

import kotlinx.datetime.Clock
import ru.mss.common.MssContext
import ru.mss.common.helpers.asMssError
import ru.mss.common.models.MssState
import ru.mss.mappers.log.toLog
import kotlin.reflect.KClass

suspend inline fun <T> IMssAppSettings.controllerHelper(
    crossinline getRequest: suspend MssContext.() -> Unit,
    crossinline toResponse: suspend MssContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MssContext(
        timeStart = Clock.System.now(),
    )
    return try {
        logger.doWithLogging(logId) {
            ctx.getRequest()
            processor.exec(ctx)
            logger.info(
                msg = "Request $logId processed for ${clazz.simpleName}",
                marker = "BIZ",
                data = ctx.toLog(logId)
            )
            ctx.toResponse()
        }
    } catch (e: Throwable) {
        logger.doWithLogging("$logId-failure") {
            ctx.state = MssState.FAILING
            ctx.errors.add(e.asMssError())
            processor.exec(ctx)
            ctx.toResponse()
        }
    }
}