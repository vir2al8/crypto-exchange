package com.crypto.logic.workers.stubs

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.common.stubs.CommonStub
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker
import com.crypto.stubs.OrderStub

fun CorChainDsl<CommonContext>.stubSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == CommonStub.SUCCESS && state == CommonState.RUNNING }
    handle {
        state = CommonState.FINISHING
        ordersResponse.addAll(OrderStub.prepareSearchList(orderFilterRequest.operation))
    }
}
