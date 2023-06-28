package com.crypto

import com.crypto.configs.OrderAppConfigs
import com.crypto.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(
    appConfigs: OrderAppConfigs = initAppConfigs()
) {
    configureSerialization()
    configureMonitoring()
    configureAuthorization(appConfigs)
    configureRouting(appConfigs)
}
