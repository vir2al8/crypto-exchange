package com.crypto.plugins

import com.crypto.common.CommonSettings
import com.crypto.configs.OrderAppConfigs
import com.crypto.configs.OrderDbType
import com.crypto.logic.OrderProcessor
import io.ktor.server.application.*

fun Application.initAppConfigs(): OrderAppConfigs {
    val corSettings = CommonSettings(
        repositoryTest = getDatabaseConf(OrderDbType.TEST),
        repositoryProd = getDatabaseConf(OrderDbType.PROD),
    )
    return OrderAppConfigs(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = OrderProcessor(),
    )
}