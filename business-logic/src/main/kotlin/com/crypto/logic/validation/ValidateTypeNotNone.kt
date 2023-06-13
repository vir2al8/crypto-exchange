package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.helpers.errorValidation
import com.crypto.common.helpers.fail
import com.crypto.common.models.CommonOrderType
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.validateTypeNotNone(title: String) = worker {
    this.title = title
    on { orderValidating.type == CommonOrderType.NONE }
    handle {
        fail(
            errorValidation(
                field = "type",
                violationCode = "none",
                description = "field must not be none"
            )
        )
    }
}
