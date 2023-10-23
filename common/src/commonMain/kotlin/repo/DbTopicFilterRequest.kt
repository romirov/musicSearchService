package ru.mss.common.repo

import ru.mss.common.models.MssTopicStatus
import ru.mss.common.models.MssUserId

data class DbTopicFilterRequest(
    val titleFilter: String = "",
    val ownerId: MssUserId = MssUserId.NONE,
    val status: MssTopicStatus = MssTopicStatus.NONE,
)
