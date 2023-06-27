package com.crypto.controller.v1

import com.crypto.api.v1.models.*
import com.crypto.authorization.addAuthorization
import com.crypto.common.models.CommonOrder
import com.crypto.common.permissions.CommonUserGroups
import com.crypto.common.repository.DbOrderResponse
import com.crypto.common.repository.DbOrdersResponse
import com.crypto.configs.KtorAuthConfig
import com.crypto.helpers.testSettings
import com.crypto.module
import com.crypto.repotest.OrderRepositoryMock
import com.crypto.stubs.OrderStub
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class StubOrderControllerTest {
    private val stub = OrderStub.get()
    private val orderId = stub.id

    @Test
    fun create() = testApplication {
        val repo = OrderRepositoryMock(
            invokeCreateOrder = {
                DbOrderResponse(
                    isSuccess = true,
                    data = it.order.copy(id = orderId),
                )
            }
        )
        application { module(testSettings(repo)) }
        val client = myClient()
        val requestObject = OrderCreateRequest(
            requestId = "123",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            ),
            order = OrderCreateObject(
                walletId = "wallet-1",
                amount = BigDecimal.valueOf(10),
                type = OrderType.MARKET,
                operation = OrderOperation.BUYING
            )
        )

        val response = client.post("/api/v1/order/create") {
            contentType(ContentType.Application.Json)
            addAuthorization(id = "wallet-1", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
            setBody(requestObject)
        }

        val responseObject = response.body<OrderCreateResponse>()
        println(responseObject)
        assertEquals(200, response.status.value)
        assertEquals("order-1", responseObject.order?.id)
        assertEquals("wallet-1", responseObject.order?.walletId)
        assertEquals(OrderType.MARKET, responseObject.order?.type)
        assertEquals(BigDecimal.valueOf(10), responseObject.order?.amount)
    }

    @Test
    fun read() = testApplication {
        val repo = OrderRepositoryMock(
            invokeReadOrder = {
                DbOrderResponse(
                    isSuccess = true,
                    data = CommonOrder(id = it.id)
                )
            }
        )
        application { module(testSettings(repo)) }
        val client = myClient()
        val requestObject = OrderReadRequest(
            requestId = "123",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            ),
            order = OrderReadObject(id = "order-1")
        )

        val response = client.post("/api/v1/order/read") {
            contentType(ContentType.Application.Json)
            addAuthorization(id = "wallet-1", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
            setBody(requestObject)
        }

        val responseObject = response.body<OrderReadResponse>()
        println(responseObject)
        assertEquals(200, response.status.value)
        assertEquals("order-1", responseObject.order?.id)
        assertEquals("wallet-1", responseObject.order?.walletId)
        assertEquals(OrderType.MARKET, responseObject.order?.type)
        assertEquals(BigDecimal.valueOf(10), responseObject.order?.amount)
    }

    @Test
    fun delete() = testApplication {
        val repo = OrderRepositoryMock(
            invokeReadOrder = {
                DbOrderResponse(
                    isSuccess = true,
                    data = CommonOrder(id = it.id)
                )
            },
            invokeDeleteOrder = {
                DbOrderResponse(
                    isSuccess = true,
                    data = CommonOrder(id = it.id)
                )
            }
        )
        application { module(testSettings(repo)) }
        val client = myClient()
        val requestObject = OrderDeleteRequest(
            requestId = "123",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            ),
            order = OrderDeleteObject(id = "order-1")
        )

        val response = client.post("/api/v1/order/delete") {
            contentType(ContentType.Application.Json)
            addAuthorization(id = "wallet-1", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
            setBody(requestObject)
        }

        val responseObject = response.body<OrderDeleteResponse>()
        println(responseObject)
        assertEquals(204, response.status.value)
    }

    @Test
    fun search() = testApplication {
        val repo = OrderRepositoryMock(
            invokeSearchOrder = {
                DbOrdersResponse(
                    isSuccess = true,
                    data = listOf(
                        CommonOrder(
                            status = it.status,
                            operation = it.operation
                        )
                    )
                )
            }
        )
        application { module(testSettings(repo)) }
        val client = myClient()
        val requestObject = OrderSearchRequest(
            requestId = "123",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            ),
            orderFilter = OrderSearchFilter(
                status = OrderStatus.OPEN,
                operation = OrderOperation.BUYING
            )
        )

        val response = client.post("/api/v1/order/search") {
            contentType(ContentType.Application.Json)
            addAuthorization(id = "wallet-1", config = KtorAuthConfig.TEST, groups = listOf(CommonUserGroups.TEST.name))
            setBody(requestObject)
        }

        val responseObject = response.body<OrderSearchResponse>()
        println(responseObject)
        assertEquals(200, response.status.value)
        assertEquals("order-1", responseObject.orders?.first()?.id)
        assertEquals("wallet-1", responseObject.orders?.first()?.walletId)
        assertEquals(OrderType.MARKET, responseObject.orders?.first()?.type)
        assertEquals(BigDecimal.valueOf(10), responseObject.orders?.first()?.amount)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}