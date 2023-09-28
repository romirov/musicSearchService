package ru.mss.common.models

data class MssTopicAnswer(
    var id: MssTopicAnswerId = MssTopicAnswerId.NONE,
    var userId: MssUserId = MssUserId.NONE,
    var answerBody: String = "",
) {
    companion object {
        val NONE = MssTopicAnswer()
    }
}