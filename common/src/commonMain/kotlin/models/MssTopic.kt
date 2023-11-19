package ru.mss.common.models

import ru.mss.common.permissions.MssPrincipalRelations

data class MssTopic(
    var id: MssTopicId = MssTopicId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: MssUserId = MssUserId.NONE,
    var status: MssTopicStatus = MssTopicStatus.NONE,
    var answers: MutableList<MssTopicAnswer> = mutableListOf(),
    var lock: MssTopicLock = MssTopicLock.NONE,
    var principalRelations: Set<MssPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<MssTopicPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): MssTopic = copy(
        permissionsClient = permissionsClient.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MssTopic()
    }
}