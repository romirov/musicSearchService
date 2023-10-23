package ru.mss.common.repo

import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId

data class DbTopicIdRequest(
    val id: MssTopicId,
) {
    constructor(topic: MssTopic): this(topic.id)
}
