package ru.mss.common.repo

import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicLock

data class DbTopicIdRequest(
    val id: MssTopicId,
    val lock: MssTopicLock = MssTopicLock.NONE,
) {
    constructor(topic: MssTopic): this(topic.id, topic.lock)
}
