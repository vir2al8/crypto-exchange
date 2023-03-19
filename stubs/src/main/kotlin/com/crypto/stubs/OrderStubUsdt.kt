package com.crypto.stubs

import com.crypto.common.models.*
import java.math.BigDecimal
import java.time.Instant

object OrderStubUsdt {
    val ORDER_BUYING_USDT: CommonOrder
        get() = CommonOrder(
            id = CommonOrderId("order-1"),
            walletId = CommonWalletId("wallet-1"),
            amount = BigDecimal(10),
            type = CommonOrderType.MARKET,
            operation = CommonOrderOperation.BUYING,
            status = CommonOrderStatus.OPEN,
            createdAt = Instant.parse("2023-03-15T20:00:00Z"),
            updatedAt = Instant.parse("2023-03-15T20:00:00Z")
        )

    val ORDER_SELLING_USDT: CommonOrder = ORDER_BUYING_USDT.copy(operation = CommonOrderOperation.SELLING)
}