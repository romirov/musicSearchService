package ru.mss.app.common

import kotlinx.datetime.Clock
import ru.mss.common.MssContext
import ru.mss.common.helpers.asMssError
import ru.mss.common.models.MssState

suspend inline fun <T> IMssAppSettings.controllerHelper(
    getRequest: MssContext.() -> Unit,
    toResponse: MssContext.() -> T,
): T {
    val ctx = MssContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        processor.exec(ctx)
        ctx.toResponse()
    } catch (e: Throwable) {
        ctx.state = MssState.FAILING
        ctx.errors.add(e.asMssError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}