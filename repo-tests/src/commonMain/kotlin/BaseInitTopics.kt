package ru.mss.repo.tests

import ru.mss.common.models.MssTopic
import ru.mss.common.models.MssTopicId
import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssUserId


abstract class BaseInitTopics(val op: String): IInitObjects<MssTopic> {

    fun createInitTestModel(
        suf: String,
        ownerId: MssUserId = MssUserId("owner-123"),
        status: MssTopicStatus = MssTopicStatus.OPENED,
    ) = MssTopic(
        id = MssTopicId("topic-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        status = status
    )
}
