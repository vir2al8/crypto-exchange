package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.common.repository.DbOrderFilterRequest
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.repositorySearch(title: String) = worker {
    this.title = title
    description = "Поиск объектов в БД по фильтру"
    on { state == CommonState.RUNNING }
    handle {
        val request = DbOrderFilterRequest(
            status = orderFilterValidated.status,
            type = orderFilterValidated.type,
            operation = orderFilterValidated.operation,
        )
        val result = orderRepository.searchOrder(request)
        val resultAds = result.data
        if (result.isSuccess && resultAds != null) {
            ordersRepositoryDone = resultAds.toMutableList()
        } else {
            state = CommonState.FAILING
            errors.addAll(result.errors)
        }
    }
}