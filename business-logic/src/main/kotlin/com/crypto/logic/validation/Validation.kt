package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.chain

fun CorChainDsl<CommonContext>.validation(block: CorChainDsl<CommonContext>.() -> Unit) = chain {
    block()
    title = "Валидация"
    on { state == CommonState.RUNNING }
}
