package ru.mss.common.models

data class MssTopic(
    var id: MssTopicId = MssTopicId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MssUserId = MssUserId.NONE,
    var songExcerpt: ByteArray = byteArrayOf(Byte.MIN_VALUE),
    var status: MssTopicStatus = MssTopicStatus.NONE,
    val answers: MutableList<MssTopicAnswer> = mutableListOf(),
    val permissionsClient: MutableSet<MssTopicPermissionClient> = mutableSetOf()
)
