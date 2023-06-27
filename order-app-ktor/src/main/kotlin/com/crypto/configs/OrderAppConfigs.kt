package com.crypto.configs

import com.crypto.common.CommonSettings
import com.crypto.logic.OrderProcessor


data class OrderAppConfigs(
    val appUrls: List<String> = emptyList(),
    val corSettings: CommonSettings,
    val processor: OrderProcessor = OrderProcessor(),
    val authorization: KtorAuthConfig = KtorAuthConfig.NONE,
)
