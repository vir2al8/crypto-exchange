package com.crypto.repotest

import com.crypto.common.models.*
import java.math.BigDecimal
import java.time.Instant

abstract class BaseInitOrders(val operation: String) : InitObjects<CommonOrder> {
    fun createInitTestModel(
        suf: String,
        walletId: CommonWalletId = CommonWalletId("wallet-id-1"),
        orderType: CommonOrderType = CommonOrderType.LIMIT,
        status: CommonOrderStatus = CommonOrderStatus.OPEN,
        operation: CommonOrderOperation = CommonOrderOperation.BUYING,
        amount: BigDecimal = BigDecimal.TEN,
    ) = CommonOrder(
        id = CommonOrderId("order-repository-$operation-$suf"),
        walletId = walletId,
        status = status,
        type = orderType,
        operation = operation,
        amount = amount,
        createdAt = Instant.parse("2023-03-03T08:05:57Z")
    )
}