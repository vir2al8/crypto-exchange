package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.helpers.errorValidation
import com.crypto.common.helpers.fail
import com.crypto.common.models.CommonOrderOperation
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.validateOperationNotNone(title: String) = worker {
    this.title = title
    on { orderValidating.operation == CommonOrderOperation.NONE }
    handle {
        fail(
            errorValidation(
                field = "operation",
                violationCode = "none",
                description = "field must not be none"
            )
        )
    }
}
