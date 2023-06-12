package com.crypto.logic.workers

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.initStatus(title: String) = worker {
    this.title = title
    on { state == CommonState.NONE }
    handle { state = CommonState.RUNNING }
}