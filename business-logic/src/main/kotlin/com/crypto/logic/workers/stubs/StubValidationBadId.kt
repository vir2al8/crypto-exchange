package ru.otus.otuskotlin.marketplace.biz.workers

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonState
import com.crypto.common.stubs.CommonStub
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.stubValidationBadId(title: String) = worker {
    this.title = title
    on { stubCase == CommonStub.BAD_ID && state == CommonState.RUNNING }
    handle {
        state = CommonState.FAILING
        this.errors.add(
            CommonError(
                group = "validation",
                code = "validation-id",
                field = "id",
                message = "Wrong id field"
            )
        )
    }
}
