package ru.mss.common.models

data class MssTopic(
    var id: MssTopicId = MssTopicId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MssUserId = MssUserId.NONE,
    var status: MssTopicStatus = MssTopicStatus.NONE,
    val answers: MutableList<MssTopicAnswer> = mutableListOf(),
    val permissionsClient: MutableSet<MssTopicPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): MssTopic = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

}