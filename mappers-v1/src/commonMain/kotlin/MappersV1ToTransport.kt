package ru.mss.mappers.v1

import ru.mss.api.v1.models.*
import ru.mss.common.MssContext
import ru.mss.common.models.*
import ru.mss.mappers.v1.exceptions.UnknownMssCommand

fun MssContext.toTransportTopic(): IResponse = when (val cmd = command) {
    MssCommand.CREATE -> toTransportCreate()
    MssCommand.READ -> toTransportRead()
    MssCommand.UPDATE -> toTransportUpdate()
    MssCommand.DELETE -> toTransportDelete()
    MssCommand.SEARCH -> toTransportSearch()
    MssCommand.NONE -> throw UnknownMssCommand(cmd)
}

fun MssContext.toTransportCreate() = TopicCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MssState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    topic = topicResponse.toTransportTopic()
)

fun MssContext.toTransportRead() = TopicReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MssState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    topic = topicResponse.toTransportTopic()
)

fun MssContext.toTransportUpdate() = TopicUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MssState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    topic = topicResponse.toTransportTopic()
)

fun MssContext.toTransportDelete() = TopicDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MssState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    topic = topicResponse.toTransportTopic()
)

fun MssContext.toTransportSearch() = TopicSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == MssState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    topics = topicsResponse.toTransportTopic()
)

fun List<MssTopic>.toTransportTopic(): List<TopicResponseObject>? = this
    .map { it.toTransportTopic() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun MssContext.toTransportInit() = TopicInitResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (errors.isEmpty()) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

private fun MssTopic.toTransportTopic(): TopicResponseObject = TopicResponseObject(
    id = id.takeIf { it != MssTopicId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MssUserId.NONE }?.asString(),
    permissions = permissionsClient.toTransportTopic(),
    status = status.toTransportTopic(),
    answers = answers.toTransportAnswers()
)

private fun Set<MssTopicPermissionClient>.toTransportTopic(): Set<TopicPermissions>? = this
    .map { it.toTransportTopic() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun MssTopicPermissionClient.toTransportTopic() = when (this) {
    MssTopicPermissionClient.READ -> TopicPermissions.READ
    MssTopicPermissionClient.UPDATE -> TopicPermissions.UPDATE
    MssTopicPermissionClient.MAKE_OPENED -> TopicPermissions.MAKE_OPENED
    MssTopicPermissionClient.MAKE_CLOSED -> TopicPermissions.MAKE_CLOSED
    MssTopicPermissionClient.DELETE -> TopicPermissions.DELETE
}

private fun MssTopicStatus.toTransportTopic(): TopicStatus? = when (this) {
    MssTopicStatus.OPENED -> TopicStatus.OPENED
    MssTopicStatus.CLOSED -> TopicStatus.CLOSED
    MssTopicStatus.NONE -> null
}

private fun MutableList<MssTopicAnswer>.toTransportAnswers(): List<Answer>? = this
    .map { it.toTransportTopic() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MssTopicAnswer.toTransportTopic() = Answer(
    id = this.id.takeIf { it != MssTopicAnswerId.NONE }?.asString(),
    userId = this.userId.takeIf { it != MssUserId.NONE }?.asString(),
    answerBody = this.answerBody.takeIf { it.isNotBlank() }
)

private fun List<MssError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportTopic() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MssError.toTransportTopic() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)
