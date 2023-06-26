package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.common.repository.DbOrderIdRequest
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.repositoryDelete(title: String) = worker {
    this.title = title
    description = "Удаление объекта из БД по id"
    on { state == CommonState.RUNNING }
    handle {
        val request = DbOrderIdRequest(orderRepositoryPrepare)
        val result = orderRepository.deleteOrder(request)
        if (!result.isSuccess) {
            state = CommonState.FAILING
            errors.addAll(result.errors)
        }
        orderRepositoryDone = orderRepositoryRead
    }
}