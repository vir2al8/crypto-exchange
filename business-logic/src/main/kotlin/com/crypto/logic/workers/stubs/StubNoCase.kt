package com.crypto.logic.workers.stubs

import com.crypto.common.CommonContext
import com.crypto.common.helpers.fail
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == CommonState.RUNNING }
    handle {
        fail(
            CommonError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
