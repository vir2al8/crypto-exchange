package com.crypto.plugins

import com.crypto.configs.OrderAppConfigs
import com.crypto.logic.OrderProcessor
import io.ktor.server.application.*

fun Application.initAppConfigs(): OrderAppConfigs {
    return OrderAppConfigs(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = OrderProcessor(),
    )
}