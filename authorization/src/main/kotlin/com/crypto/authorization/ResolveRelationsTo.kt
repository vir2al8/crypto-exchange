package com.crypto.authorization

import com.crypto.common.models.CommonOrder
import com.crypto.common.permissions.CommonPrincipalModel
import com.crypto.common.permissions.CommonPrincipalRelations

fun CommonOrder.resolveRelationsTo(principal: CommonPrincipalModel): Set<CommonPrincipalRelations> = setOfNotNull(
    CommonPrincipalRelations.NONE,
    CommonPrincipalRelations.OWN.takeIf { principal.id == walletId },
)
