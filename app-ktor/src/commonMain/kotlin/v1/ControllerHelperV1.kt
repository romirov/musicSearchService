package ru.mss.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.IResponse
import ru.mss.app.common.controllerHelper
import ru.mss.app.ktor.MssAppSettings
import ru.mss.app.ktor.base.toModel
import ru.mss.mappers.v1.fromTransport
import ru.mss.mappers.v1.toTransportTopic
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: MssAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        principal = this@processV1.request.call.principal<JWTPrincipal>().toModel()
        fromTransport(receive<Q>())
    },
    { respond(toTransportTopic()) },
    clazz,
    logId,
)