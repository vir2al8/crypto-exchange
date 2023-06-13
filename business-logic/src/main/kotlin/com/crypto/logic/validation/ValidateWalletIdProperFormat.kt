package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.helpers.errorValidation
import com.crypto.common.helpers.fail
import com.crypto.common.models.CommonWalletId
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.validateWalletIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { orderValidating.walletId != CommonWalletId.NONE && !orderValidating.walletId.asString().matches(regExp) }
    handle {
        val encodedId = orderValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "walletId",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
