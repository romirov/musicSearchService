package ru.mss.repo.inmemory.model

import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssUserId

data class TopicEntity (
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null,
    val status: String? = null,
) {
    constructor(model: MssTopic): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        status = model.status.takeIf { it != MssTopicStatus.NONE }?.name,
    )

    fun toInternal() = MssTopic(
        id = id?.let { MssTopicId(it) }?: MssTopicId.NONE,
        title = title?: "",
        description = description?: "",
        ownerId = ownerId?.let { MssUserId(it) }?: MssUserId.NONE,
        status = status?.let { MssTopicStatus.valueOf(it) }?: MssTopicStatus.NONE
    )
}
