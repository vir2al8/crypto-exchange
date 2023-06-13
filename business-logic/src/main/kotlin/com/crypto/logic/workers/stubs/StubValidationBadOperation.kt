package com.crypto.logic.workers.stubs

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonState
import com.crypto.common.stubs.CommonStub
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.stubValidationBadOperation(title: String) = worker {
    this.title = title
    on { stubCase == CommonStub.BAD_OPERATION && state == CommonState.RUNNING }
    handle {
        state = CommonState.FAILING
        this.errors.add(
            CommonError(
                group = "validation",
                code = "validation-operation",
                field = "operation",
                message = "Wrong operation field"
            )
        )
    }
}
