package com.crypto.common.models

import com.crypto.common.permissions.CommonOrderPermissionClient
import com.crypto.common.permissions.CommonPrincipalRelations
import java.math.BigDecimal
import java.time.Instant

data class CommonOrder(
    var id: CommonOrderId = CommonOrderId.NONE,
    var walletId: CommonWalletId = CommonWalletId.NONE,
    var amount: BigDecimal = BigDecimal.ZERO,
    var type: CommonOrderType = CommonOrderType.NONE,
    var operation: CommonOrderOperation = CommonOrderOperation.NONE,
    var status: CommonOrderStatus = CommonOrderStatus.NONE,
    var createdAt: Instant = Instant.MIN,
    var updatedAt: Instant = Instant.MIN,
    var principalRelations: Set<CommonPrincipalRelations> = emptySet(),
    val permissionsClient: MutableSet<CommonOrderPermissionClient> = mutableSetOf()
) {
    fun deepCopy(): CommonOrder = copy(
        principalRelations = principalRelations.toSet(),
        permissionsClient = permissionsClient.toMutableSet(),
    )

    fun isEmpty() = this == NONE

    companion object {
        val NONE = CommonOrder()
    }
}