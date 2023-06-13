package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.helpers.errorValidation
import com.crypto.common.helpers.fail
import com.crypto.common.models.CommonOrderId
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.validateIdProperFormat(title: String) = worker {
    this.title = title
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { orderValidating.id != CommonOrderId.NONE && !orderValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = orderValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
