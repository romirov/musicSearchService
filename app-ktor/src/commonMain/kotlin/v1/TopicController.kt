package ru.mss.app.ktor.v1

import io.ktor.server.application.*
import ru.mss.api.v1.models.*
import ru.mss.app.ktor.MssAppSettings
import kotlin.reflect.KClass

private val clCreate: KClass<*> = ApplicationCall::createTopic::class
suspend fun ApplicationCall.createTopic(appSettings: MssAppSettings) =
    processV1<TopicCreateRequest, TopicCreateResponse>(appSettings, clCreate, "createTopic")

private val clRead: KClass<*> = ApplicationCall::readTopic::class
suspend fun ApplicationCall.readTopic(appSettings: MssAppSettings) =
    processV1<TopicReadRequest, TopicReadResponse>(appSettings, clRead, "readTopic")

private val clUpdate: KClass<*> = ApplicationCall::updateTopic::class
suspend fun ApplicationCall.updateTopic(appSettings: MssAppSettings) =
    processV1<TopicUpdateRequest, TopicUpdateResponse>(appSettings, clUpdate, "updateTopic")

private val clDelete: KClass<*> = ApplicationCall::deleteTopic::class
suspend fun ApplicationCall.deleteTopic(appSettings: MssAppSettings) =
    processV1<TopicDeleteRequest, TopicDeleteResponse>(appSettings, clDelete, "deleteTopic")

private val clSearch: KClass<*> = ApplicationCall::searchTopic::class
suspend fun ApplicationCall.searchTopic(appSettings: MssAppSettings) =
    processV1<TopicSearchRequest, TopicSearchResponse>(appSettings, clSearch, "searchTopic")
