package com.crypto.configs

import com.crypto.logic.OrderProcessor


data class OrderAppConfigs(
    val appUrls: List<String> = emptyList(),
    val processor: OrderProcessor = OrderProcessor(),
)
