package ru.mss.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.IResponse
import ru.mss.app.ktor.MssAppSettings
import ru.mss.common.MssContext
import ru.mss.common.helpers.asMssError
import ru.mss.common.models.MssCommand
import ru.mss.common.models.MssState
import ru.mss.lib.logging.common.IMssLogWrapper
import ru.mss.mappers.log.toLog
import ru.mss.mappers.v1.fromTransport
import ru.mss.mappers.v1.toTransportTopic

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: MssAppSettings,
    logger: IMssLogWrapper,
    logId: String,
    command: MssCommand? = null,
) {
    val ctx = MssContext(
        timeStart = Clock.System.now(),
    )
    val processor = appSettings.processor
    try {
        logger.doWithLogging(id = logId) {
            val request = receive<Q>()
            ctx.fromTransport(request)
            logger.info(
                msg = "$command request is got",
                data = ctx.toLog("${logId}-got")
            )
            processor.exec(ctx)
            logger.info(
                msg = "$command request is handled",
                data = ctx.toLog("${logId}-handled")
            )
            respond(ctx.toTransportTopic())
        }
    } catch (e: Throwable) {
        logger.doWithLogging(id = "${logId}-failure") {
            command?.also { ctx.command = it }
            logger.error(
                msg = "$command handling failed",
            )
            ctx.state = MssState.FAILING
            ctx.errors.add(e.asMssError())
            processor.exec(ctx)
            respond(ctx.toTransportTopic())
        }
    }
}