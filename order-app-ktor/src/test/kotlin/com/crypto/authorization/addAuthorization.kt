package com.crypto.authorization

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.crypto.configs.KtorAuthConfig
import io.ktor.client.request.*
import io.ktor.http.*

fun HttpRequestBuilder.addAuthorization(
    id: String,
    groups: List<String>,
    config: KtorAuthConfig
) {
    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim(KtorAuthConfig.GROUPS_CLAIM, groups)
        .withClaim(KtorAuthConfig.ID_CLAIM, id)
        .sign(Algorithm.none()) // TODO

    header(HttpHeaders.Authorization, "Bearer $token")
}