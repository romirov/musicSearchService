package ru.mss.auth

import ru.mss.common.models.MssTopicPermissionClient
import ru.mss.common.permissions.MssPrincipalRelations
import ru.mss.common.permissions.MssUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<MssUserPermissions>,
    relations: Iterable<MssPrincipalRelations>,
) = mutableSetOf<MssTopicPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // READ
    MssUserPermissions.READ_OWN to mapOf(
        MssPrincipalRelations.OWN to MssTopicPermissionClient.READ
    ),
    MssUserPermissions.READ_GROUP to mapOf(
        MssPrincipalRelations.GROUP to MssTopicPermissionClient.READ
    ),
    MssUserPermissions.READ_PUBLIC to mapOf(
        MssPrincipalRelations.PUBLIC to MssTopicPermissionClient.READ
    ),
    MssUserPermissions.READ_CANDIDATE to mapOf(
        MssPrincipalRelations.MODERATABLE to MssTopicPermissionClient.READ
    ),

    // UPDATE
    MssUserPermissions.UPDATE_OWN to mapOf(
        MssPrincipalRelations.OWN to MssTopicPermissionClient.UPDATE
    ),
    MssUserPermissions.UPDATE_PUBLIC to mapOf(
        MssPrincipalRelations.MODERATABLE to MssTopicPermissionClient.UPDATE
    ),
    MssUserPermissions.UPDATE_CANDIDATE to mapOf(
        MssPrincipalRelations.MODERATABLE to MssTopicPermissionClient.UPDATE
    ),

    // DELETE
    MssUserPermissions.DELETE_OWN to mapOf(
        MssPrincipalRelations.OWN to MssTopicPermissionClient.DELETE
    ),
    MssUserPermissions.DELETE_PUBLIC to mapOf(
        MssPrincipalRelations.MODERATABLE to MssTopicPermissionClient.DELETE
    ),
    MssUserPermissions.DELETE_CANDIDATE to mapOf(
        MssPrincipalRelations.MODERATABLE to MssTopicPermissionClient.DELETE
    ),
)
