package com.crypto.authorization

import com.crypto.configs.KtorAuthConfig
import com.crypto.helpers.testSettings
import com.crypto.module
import io.ktor.client.request.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class AuthorizationTest {
    @Test
    fun invalidAudience() = testApplication {
        application { module(testSettings()) }


        val response = client.post("api/v1/order/create") {
            addAuthorization(id = "test", config = KtorAuthConfig.TEST.copy(audience = "invalid"), groups = listOf())
        }

        assertEquals(401, response.status.value)
    }
}