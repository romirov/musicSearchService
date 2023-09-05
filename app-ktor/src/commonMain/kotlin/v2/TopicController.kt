package ru.mss.app.ktor.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.mss.api.v1.models.*
import ru.mss.biz.MssTopicProcessor
import ru.mss.common.MssContext
import ru.mss.mappers.v1.*

suspend fun ApplicationCall.createTopic(processor: MssTopicProcessor) {
    val request = receive<TopicCreateRequest>()
    val context = MssContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.readTopic(processor: MssTopicProcessor) {
    val request = receive<TopicReadRequest>()
    val context = MssContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.updateTopic(processor: MssTopicProcessor) {
    val request = receive<TopicUpdateRequest>()
    val context = MssContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.deleteTopic(processor: MssTopicProcessor) {
    val request = receive<TopicDeleteRequest>()
    val context = MssContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.searchTopic(processor: MssTopicProcessor) {
    val request = receive<TopicSearchRequest>()
    val context = MssContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportSearch())
}
