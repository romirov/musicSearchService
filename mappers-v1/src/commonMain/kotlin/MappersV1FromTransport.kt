package ru.mss.mappers.v1

import ru.mss.api.v1.models.*
import ru.mss.common.MssContext
import ru.mss.common.models.*
import ru.mss.common.stubs.MssStubs
import ru.mss.mappers.v1.exceptions.UnknownRequestClass

fun MssContext.fromTransport(request: IRequest) = when (request) {
    is TopicCreateRequest -> fromTransport(request)
    is TopicReadRequest -> fromTransport(request)
    is TopicUpdateRequest -> fromTransport(request)
    is TopicDeleteRequest -> fromTransport(request)
    is TopicSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request::class)
}

private fun String?.toTopicId() = this?.let { MssTopicId(it) } ?: MssTopicId.NONE
private fun String?.toAnswerId() = this?.let { MssTopicAnswerId(it) } ?: MssTopicAnswerId.NONE
private fun String?.toTopicWithId() = MssTopic(id = this.toTopicId())
private fun IRequest?.requestId() = this?.requestId?.let { MssRequestId(it) } ?: MssRequestId.NONE

private fun TopicDebug?.transportToWorkMode(): MssWorkMode = when (this?.mode) {
    TopicRequestDebugMode.PROD -> MssWorkMode.PROD
    TopicRequestDebugMode.TEST -> MssWorkMode.TEST
    TopicRequestDebugMode.STUB -> MssWorkMode.STUB
    null -> MssWorkMode.PROD
}

private fun TopicDebug?.transportToStubCase(): MssStubs = when (this?.stub) {
    TopicRequestDebugStubs.SUCCESS -> MssStubs.SUCCESS
    TopicRequestDebugStubs.NOT_FOUND -> MssStubs.NOT_FOUND
    TopicRequestDebugStubs.BAD_ID -> MssStubs.BAD_ID
    TopicRequestDebugStubs.BAD_TITLE -> MssStubs.BAD_TITLE
    TopicRequestDebugStubs.BAD_DESCRIPTION -> MssStubs.BAD_DESCRIPTION
    TopicRequestDebugStubs.CANNOT_DELETE -> MssStubs.CANNOT_DELETE
    TopicRequestDebugStubs.BAD_SEARCH_STRING -> MssStubs.BAD_SEARCH_STRING
    TopicRequestDebugStubs.BAD_STATUS -> MssStubs.BAD_STATUS
    null -> MssStubs.NONE
}

fun MssContext.fromTransport(request: TopicCreateRequest) {
    command = MssCommand.CREATE
    requestId = request.requestId()
    topicRequest = request.topic?.toInternal() ?: MssTopic()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MssContext.fromTransport(request: TopicReadRequest) {
    command = MssCommand.READ
    requestId = request.requestId()
    topicRequest = request.topic?.id.toTopicWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MssContext.fromTransport(request: TopicUpdateRequest) {
    command = MssCommand.UPDATE
    requestId = request.requestId()
    topicRequest = request.topic?.toInternal() ?: MssTopic()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MssContext.fromTransport(request: TopicDeleteRequest) {
    command = MssCommand.DELETE
    requestId = request.requestId()
    topicRequest = request.topic?.id.toTopicWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MssContext.fromTransport(request: TopicSearchRequest) {
    command = MssCommand.SEARCH
    requestId = request.requestId()
    topicFilterRequest = request.topicFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun TopicSearchFilter?.toInternal(): MssTopicFilter = MssTopicFilter(
    searchString = this?.searchString ?: ""
)

private fun TopicCreateObject.toInternal(): MssTopic = MssTopic(
    title = this.title ?: "",
    description = this.description ?: "",
    status = this.status.fromTransport()
)

private fun TopicUpdateObject.toInternal(): MssTopic = MssTopic(
    id = this.id.toTopicId(),
    title = this.title ?: "",
    description = this.description ?: "",
    answers = this.answer.fromTransport()
)

private fun Answer?.fromTransport(): MutableList<MssTopicAnswer> = when (this) {
    null -> mutableListOf()
    else -> mutableListOf(
        MssTopicAnswer(
            id = this.id.toAnswerId(),
            answerBody = this.answerBody ?: ""
        )
    )
}

private fun TopicStatus?.fromTransport(): MssTopicStatus = when (this) {
    TopicStatus.OPENED -> MssTopicStatus.OPENED
    TopicStatus.CLOSED -> MssTopicStatus.CLOSED
    null -> MssTopicStatus.NONE
}