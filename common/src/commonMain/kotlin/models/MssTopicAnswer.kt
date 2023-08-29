package ru.mss.common.models

data class MssTopicAnswer (
    var userId: MssUserId = MssUserId.NONE,
    var answerBody: String = "",
)