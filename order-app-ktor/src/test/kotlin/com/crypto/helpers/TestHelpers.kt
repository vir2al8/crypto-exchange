package com.crypto.helpers

import com.crypto.common.CommonSettings
import com.crypto.common.repository.OrderRepository
import com.crypto.configs.OrderAppConfigs
import com.crypto.repostub.OrderRepositoryStub

fun testSettings(repo: OrderRepository? = null) = OrderAppConfigs(
    corSettings = CommonSettings(
        repoStub = OrderRepositoryStub(),
        repoTest = repo ?: OrderRepositoryStub(),
        repoProd = repo ?: OrderRepositoryStub(),
    ),
)
