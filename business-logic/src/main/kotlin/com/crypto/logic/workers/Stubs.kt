package com.crypto.logic.workers

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.common.models.CommonWorkMode
import com.crypto.cor.CorChainDsl
import com.crypto.cor.chain

fun CorChainDsl<CommonContext>.stubs(title: String, block: CorChainDsl<CommonContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == CommonWorkMode.STUB && state == CommonState.RUNNING }
}
