package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.repositoryPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные к удалению из БД".trimIndent()
    on { state == CommonState.RUNNING }
    handle {
        orderRepositoryPrepare = orderValidated.deepCopy()
    }
}