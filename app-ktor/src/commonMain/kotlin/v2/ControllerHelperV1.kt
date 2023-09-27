package ru.mss.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.IResponse
import ru.mss.app.common.controllerHelper
import ru.mss.app.ktor.MssAppSettings
import ru.mss.mappers.v1.fromTransport
import ru.mss.mappers.v1.toTransportTopic
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: MssAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    { fromTransport(this@processV1.receive<Q>()) },
    { this@processV1.respond(toTransportTopic()) },
    clazz,
    logId,
)