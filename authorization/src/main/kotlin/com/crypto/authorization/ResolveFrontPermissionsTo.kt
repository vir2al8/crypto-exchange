package com.crypto.authorization

import com.crypto.common.permissions.CommonOrderPermissionClient
import com.crypto.common.permissions.CommonPrincipalRelations
import com.crypto.common.permissions.CommonUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<CommonUserPermissions>,
    relations: Iterable<CommonPrincipalRelations>,
) = mutableSetOf<CommonOrderPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

private val accessTable = mapOf(
    // Create
    CommonUserPermissions.CREATE_OWN to mapOf(
        CommonPrincipalRelations.OWN to CommonOrderPermissionClient.CREAT
    ),

    // Read
    CommonUserPermissions.READ_OWN to mapOf(
        CommonPrincipalRelations.OWN to CommonOrderPermissionClient.READ
    ),
    CommonUserPermissions.READ_ALL to mapOf(
        CommonPrincipalRelations.NONE to CommonOrderPermissionClient.READ
    ),

    // Delete
    CommonUserPermissions.DELETE_OWN to mapOf(
        CommonPrincipalRelations.OWN to CommonOrderPermissionClient.DELETE
    ),
)
