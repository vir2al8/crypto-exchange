package com.crypto.logic.workers

import com.crypto.common.CommonContext
import com.crypto.common.helpers.errorAdministration
import com.crypto.common.helpers.fail
import com.crypto.common.models.CommonWorkMode
import com.crypto.common.repository.OrderRepository
import com.crypto.cor.CorChainDsl
import com.crypto.cor.worker

fun CorChainDsl<CommonContext>.initRepository(title: String) = worker {
    this.title = title
    description = "Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы".trimIndent()
    handle {
        orderRepository = when (workMode) {
            CommonWorkMode.TEST -> settings.repositoryTest
            CommonWorkMode.STUB -> settings.repositoryStub
            else -> settings.repositoryProd
        }
        if (workMode != CommonWorkMode.STUB && orderRepository == OrderRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen work mode ($workMode). Please, contact the administrator staff"
            )
        )
    }
}