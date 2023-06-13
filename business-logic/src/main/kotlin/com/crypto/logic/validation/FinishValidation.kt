package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.finishOrderValidation(title: String) = worker {
    this.title = title
    on { state == CommonState.RUNNING }
    handle {
        orderValidated = orderValidating
    }
}

fun CorChainDsl<CommonContext>.finishOrderFilterValidation(title: String) = worker {
    this.title = title
    on { state == CommonState.RUNNING }
    handle {
        orderFilterValidated = orderFilterValidating
    }
}
