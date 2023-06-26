package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.common.repository.DbOrderRequest
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.repositoryCreate(title: String) = worker {
    this.title = title
    description = "Добавление объекта в БД"
    on { state == CommonState.RUNNING }
    handle {
        val request = DbOrderRequest(orderRepositoryPrepare)
        val result = orderRepository.createOrder(request)
        val resultOrder = result.data
        if (result.isSuccess && resultOrder != null) {
            orderRepositoryDone = resultOrder
        } else {
            state = CommonState.FAILING
            errors.addAll(result.errors)
        }
    }
}