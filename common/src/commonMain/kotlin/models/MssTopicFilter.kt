package ru.mss.common.models

data class MssTopicFilter(
    var searchString: String = "",
    var ownerId: MssUserId = MssUserId.NONE,
)
