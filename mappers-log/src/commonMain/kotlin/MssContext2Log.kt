package ru.mss.mappers.log

import kotlinx.datetime.Clock
import ru.mss.common.MssContext
import ru.mss.common.models.*

fun MssContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-musicsearchservice",
    ad = toMkplLog(),
    errors = errors.map { it.toLog() },
)

fun MssContext.toMkplLog():MssLogModel? {
    val adNone = MssTopic()
    return MssLogModel(
        requestId = requestId.takeIf { it != MssRequestId.NONE }?.asString(),
        operation = command.toLogModel(),
        requestAd = topicRequest.takeIf { it != adNone }?.toLog(),
        responseAd = topicResponse.takeIf { it != adNone }?.toLog(),
        responseAds = topicsResponse.takeIf { it.isNotEmpty() }?.filter { it != adNone }?.map { it.toLog() },
        requestFilter = topicFilterRequest.takeIf { it != MssTopicFilter() }?.toLog(),
    ).takeIf { it != MssLogModel() }
}

private fun MssCommand.toLogModel(): MssLogModel.Operation? = when(this) {
    MssCommand.CREATE -> MssLogModel.Operation.CREATE
    MssCommand.READ -> MssLogModel.Operation.READ
    MssCommand.UPDATE -> MkplLogModel.Operation.UPDATE
    MssCommand.DELETE -> MssLogModel.Operation.DELETE
    MssCommand.SEARCH -> MssLogModel.Operation.SEARCH
    MssCommand.INIT -> MssLogModel.Operation.INIT
    MssCommand.FINISH -> MssLogModel.Operation.FINISH
    MssCommand.NONE -> null
}

private fun MssTopicFilter.toLog() = TopicFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MssUserId.NONE }?.asString(),
)

fun MssError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

fun MssTopic.toLog() = TopicLog(
    id = id.takeIf { it != MssTopicId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != MssUserId.NONE }?.asString(),
    status = status.takeIf { it != MssTopicStatus.NONE }?.name,
    answers = answers.takeIf { it.isNotEmpty() } ?: emptyList<MssTopicAnswer>(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
)