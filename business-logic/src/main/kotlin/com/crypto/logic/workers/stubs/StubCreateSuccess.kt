package com.crypto.logic.workers.stubs

import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.common.stubs.CommonStub
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker
import com.crypto.stubs.OrderStub
import java.math.BigDecimal
import java.time.Instant

fun CorChainDsl<CommonContext>.stubCreateSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CommonStub.SUCCESS && state == CommonState.RUNNING }
    handle {
        state = CommonState.FINISHING
        val stub = OrderStub.prepareResult {
            orderRequest.walletId.takeIf { it != CommonWalletId.NONE }?.also { this.walletId = it }
            orderRequest.amount.takeIf { it != BigDecimal.ZERO }?.also { this.amount = it }
            orderRequest.operation.takeIf { it != CommonOrderOperation.NONE }?.also { this.operation = it }
            orderRequest.type.takeIf { it != CommonOrderType.NONE }?.also { this.type = it }
            orderRequest.status.takeIf { it != CommonOrderStatus.NONE }?.also { this.status = it }
            orderRequest.createdAt.takeIf { it != Instant.MIN }?.also { this.createdAt = it }
            orderRequest.updatedAt.takeIf { it != Instant.MIN }?.also { this.updatedAt = it }
        }
        orderResponse = stub
    }
}
