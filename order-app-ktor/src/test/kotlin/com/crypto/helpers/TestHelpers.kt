package com.crypto.helpers

import com.crypto.common.CommonSettings
import com.crypto.common.repository.OrderRepository
import com.crypto.configs.KtorAuthConfig
import com.crypto.configs.OrderAppConfigs
import com.crypto.repostub.OrderRepositoryStub

fun testSettings(repo: OrderRepository? = null) = OrderAppConfigs(
    corSettings = CommonSettings(
        repositoryStub = OrderRepositoryStub(),
        repositoryTest = repo ?: OrderRepositoryStub(),
        repositoryProd = repo ?: OrderRepositoryStub(),
    ),
    authorization = KtorAuthConfig.TEST
)
