package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.common.repository.DbOrderIdRequest
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.repositoryRead(title: String) = worker {
    this.title = title
    description = "Чтение объекта из БД"
    on { state == CommonState.RUNNING }
    handle {
        val request = DbOrderIdRequest(orderValidated)
        val result = orderRepository.readOrder(request)
        val resultOrder = result.data
        if (result.isSuccess && resultOrder != null) {
            orderRepositoryRead = resultOrder
        } else {
            state = CommonState.FAILING
            errors.addAll(result.errors)
        }
    }
}