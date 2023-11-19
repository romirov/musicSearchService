package ru.mss.app.ktor.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.mss.api.v1.models.IRequest
import ru.mss.api.v1.models.IResponse
import ru.mss.app.common.controllerHelper
import ru.mss.app.ktor.MssAppSettings
import ru.mss.common.models.MssUserId
import ru.mss.common.permissions.MssPrincipalModel
import ru.mss.common.permissions.MssUserGroups
import ru.mss.mappers.v1.fromTransport
import ru.mss.mappers.v1.toTransportTopic
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV2(
    appSettings: MssAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        principal = mssPrincipal(appSettings)
        fromTransport(this@processV2.receive<Q>())
    },
    { this@processV2.respond(toTransportTopic()) },
    clazz,
    logId,
)

// TODO: костыль для решения проблемы отсутствия jwt в native
@Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER")
fun ApplicationCall.mssPrincipal(appSettings: MssAppSettings): MssPrincipalModel = MssPrincipalModel(
    id = MssUserId("user-1"),
    fname = "Ivan",
    mname = "Ivanovich",
    lname = "Ivanov",
    groups = setOf(MssUserGroups.TEST, MssUserGroups.USER),
)