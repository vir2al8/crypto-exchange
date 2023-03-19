package com.crypto.plugins

import com.crypto.api.apiObjectMapper
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            setConfig(apiObjectMapper.serializationConfig)
            setConfig(apiObjectMapper.deserializationConfig)
        }
    }
}
