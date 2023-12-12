package ru.mss.auth

import ru.mss.common.models.MssCommand
import ru.mss.common.permissions.MssPrincipalRelations
import ru.mss.common.permissions.MssUserPermissions

fun checkPermitted(
    command: MssCommand,
    relations: Iterable<MssPrincipalRelations>,
    permissions: Iterable<MssUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }

private data class AccessTableConditions(
    val command: MssCommand,
    val permission: MssUserPermissions,
    val relation: MssPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = MssCommand.CREATE,
        permission = MssUserPermissions.CREATE_OWN,
        relation = MssPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = MssCommand.READ,
        permission = MssUserPermissions.READ_OWN,
        relation = MssPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = MssCommand.READ,
        permission = MssUserPermissions.READ_PUBLIC,
        relation = MssPrincipalRelations.PUBLIC,
    ) to true,

    // Update
    AccessTableConditions(
        command = MssCommand.UPDATE,
        permission = MssUserPermissions.UPDATE_OWN,
        relation = MssPrincipalRelations.OWN,
    ) to true,

    // Update
    AccessTableConditions(
        command = MssCommand.UPDATE,
        permission = MssUserPermissions.UPDATE_PUBLIC,
        relation = MssPrincipalRelations.NONE,
    ) to true,

    // Delete
    AccessTableConditions(
        command = MssCommand.DELETE,
        permission = MssUserPermissions.DELETE_OWN,
        relation = MssPrincipalRelations.OWN,
    ) to true,
)
