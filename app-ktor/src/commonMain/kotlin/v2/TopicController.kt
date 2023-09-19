package ru.mss.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.mss.api.v1.models.*
import ru.mss.app.ktor.MssAppSettings
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.mappers.v1.*

suspend fun ApplicationCall.createTopic(appSettings: MssAppSettings) =
    processV1<TopicCreateRequest, TopicCreateResponse>(appSettings)

suspend fun ApplicationCall.readTopic(appSettings: MssAppSettings) =
    processV1<TopicReadRequest, TopicReadResponse>(appSettings)

suspend fun ApplicationCall.updateTopic(appSettings: MssAppSettings) =
    processV1<TopicUpdateRequest, TopicUpdateResponse>(appSettings)

suspend fun ApplicationCall.deleteTopic(appSettings: MssAppSettings) =
    processV1<TopicDeleteRequest, TopicDeleteResponse>(appSettings)

suspend fun ApplicationCall.searchTopic(appSettings: MssAppSettings) =
    processV1<TopicSearchRequest, TopicSearchResponse>(appSettings)
