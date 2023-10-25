package ru.mss.repo.tests

import ru.mss.common.models.*


abstract class BaseInitTopics(val op: String): IInitObjects<MssTopic> {
    open val lockOld: MssTopicLock = MssTopicLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: MssTopicLock = MssTopicLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: MssUserId = MssUserId("owner-123"),
        status: MssTopicStatus = MssTopicStatus.OPENED,
        lock: MssTopicLock = lockOld,
    ) = MssTopic(
        id = MssTopicId("topic-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        status = status,
        lock = lock,
    )
}
