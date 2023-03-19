package com.crypto.common.models

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
    var updatedAt: Instant = Instant.MIN
)