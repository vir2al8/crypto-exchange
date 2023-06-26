package com.crypto.logic.workers

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.common.models.CommonWorkMode
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != CommonWorkMode.STUB }
    handle {
        orderResponse = orderRepositoryDone
        ordersResponse = ordersRepositoryDone
        state = when (val st = state) {
            CommonState.RUNNING -> CommonState.FINISHING
            else -> st
        }
    }
}