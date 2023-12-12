package ru.mss.auth

import ru.mss.common.permissions.MssUserGroups
import ru.mss.common.permissions.MssUserPermissions
import java.util.Collections.addAll

fun resolveChainPermissions(
    groups: Iterable<MssUserGroups>,
) = mutableSetOf<MssUserPermissions>()
    .apply {
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() }.toSet())
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    MssUserGroups.USER to setOf(
        MssUserPermissions.READ_OWN,
        MssUserPermissions.READ_PUBLIC,
        MssUserPermissions.CREATE_OWN,
        MssUserPermissions.UPDATE_PUBLIC,
        MssUserPermissions.DELETE_OWN,
    ),
    MssUserGroups.MODERATOR_MP to setOf(),
    MssUserGroups.ADMIN_AD to setOf(),
    MssUserGroups.TEST to setOf(),
    MssUserGroups.BAN_AD to setOf(),
)

private val groupPermissionsDenys = mapOf(
    MssUserGroups.USER to setOf(),
    MssUserGroups.MODERATOR_MP to setOf(),
    MssUserGroups.ADMIN_AD to setOf(),
    MssUserGroups.TEST to setOf(),
    MssUserGroups.BAN_AD to setOf(
        MssUserPermissions.UPDATE_OWN,
        MssUserPermissions.CREATE_OWN,
    ),
)
