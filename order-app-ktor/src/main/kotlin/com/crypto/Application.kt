package com.crypto

import com.crypto.configs.OrderAppConfigs
import com.crypto.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 7101, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(
    appConfigs: OrderAppConfigs = initAppConfigs()
) {
    configureSerialization()
    configureMonitoring()
    configureAuthorization(appConfigs)
    configureRouting(appConfigs)
}
