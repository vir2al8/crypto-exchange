package ru.otus.otuskotlin.marketplace.biz.workers

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonState
import com.crypto.common.stubs.CommonStub
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == CommonStub.DB_ERROR && state == CommonState.RUNNING }
    handle {
        state = CommonState.FAILING
        this.errors.add(
            CommonError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
