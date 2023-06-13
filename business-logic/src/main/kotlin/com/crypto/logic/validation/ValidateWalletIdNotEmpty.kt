package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.helpers.errorValidation
import com.crypto.common.helpers.fail
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.validateWalletIdNotEmpty(title: String) = worker {
    this.title = title
    on { orderValidating.walletId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "walledId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
