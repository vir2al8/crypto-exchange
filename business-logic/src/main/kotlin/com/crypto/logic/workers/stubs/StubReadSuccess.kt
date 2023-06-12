package ru.otus.otuskotlin.marketplace.biz.workers

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonOrderId
import com.crypto.common.models.CommonState
import com.crypto.common.stubs.CommonStub
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker
import com.crypto.stubs.OrderStub

fun CorChainDsl<CommonContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CommonStub.SUCCESS && state == CommonState.RUNNING }
    handle {
        state = CommonState.FINISHING
        val stub = OrderStub.prepareResult {
            orderRequest.id.takeIf { it != CommonOrderId.NONE }?.also { this.id = it }
        }
        orderResponse = stub
    }
}
