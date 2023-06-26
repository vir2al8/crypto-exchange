package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.repositoryPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == CommonState.RUNNING }
    handle {
        orderRepositoryRead = orderValidated.deepCopy()
        orderRepositoryPrepare = orderRepositoryRead
    }
}