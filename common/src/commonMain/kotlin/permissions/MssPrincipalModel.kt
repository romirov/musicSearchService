package ru.mss.common.permissions

import ru.mss.common.models.MssUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MssUserGroups

data class MssPrincipalModel(
    val id: MssUserId = MssUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<MssUserGroups> = emptySet()
) {
    companion object {
        val NONE = MssPrincipalModel()
    }
}
