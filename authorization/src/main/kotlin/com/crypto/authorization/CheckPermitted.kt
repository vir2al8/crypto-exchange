package com.crypto.authorization

import com.crypto.common.models.CommonCommand
import com.crypto.common.permissions.CommonPrincipalRelations
import com.crypto.common.permissions.CommonUserPermissions

fun checkPermitted(
    command: CommonCommand,
    relations: Iterable<CommonPrincipalRelations>,
    permissions: Iterable<CommonUserPermissions>,
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
    val command: CommonCommand,
    val permission: CommonUserPermissions,
    val relation: CommonPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = CommonCommand.CREATE,
        permission = CommonUserPermissions.CREATE_OWN,
        relation = CommonPrincipalRelations.OWN,
    ) to true,

    // Read
    AccessTableConditions(
        command = CommonCommand.READ,
        permission = CommonUserPermissions.READ_OWN,
        relation = CommonPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = CommonCommand.READ,
        permission = CommonUserPermissions.READ_ALL,
        relation = CommonPrincipalRelations.NONE,
    ) to true,

    // Delete
    AccessTableConditions(
        command = CommonCommand.DELETE,
        permission = CommonUserPermissions.DELETE_OWN,
        relation = CommonPrincipalRelations.OWN,
    ) to true,

    // Search
    AccessTableConditions(
        command = CommonCommand.SEARCH,
        permission = CommonUserPermissions.READ_ALL,
        relation = CommonPrincipalRelations.NONE,
    ) to true,
)
