package com.crypto.logic.groups

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonCommand
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.chain

fun CorChainDsl<CommonContext>.operation(
    title: String,
    command: CommonCommand,
    block: CorChainDsl<CommonContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == CommonState.RUNNING }
}
