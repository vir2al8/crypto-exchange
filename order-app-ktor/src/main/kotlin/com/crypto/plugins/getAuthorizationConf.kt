package com.crypto.plugins

import com.crypto.configs.KtorAuthConfig
import io.ktor.server.application.*

fun Application.getAuthorizationConfig(): KtorAuthConfig = KtorAuthConfig(
    secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "",
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString(),
    clientId = environment.config.property("jwt.clientId").getString(),
    certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString(),
)