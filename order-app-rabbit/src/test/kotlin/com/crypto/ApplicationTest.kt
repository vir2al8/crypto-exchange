package com.crypto

import com.crypto.api.apiObjectMapper
import com.crypto.api.v1.models.*
import com.crypto.configs.RabbitConfig
import com.crypto.configs.RabbitExchangeConfiguration
import com.crypto.controller.OrderController
import com.crypto.processor.RabbitDirectProcessor
import com.crypto.stubs.OrderStub
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.testcontainers.containers.RabbitMQContainer
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class ApplicationTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
    }

    val container by lazy {
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser("guest", "guest")
            start()
        }
    }

    val rabbitMqTestPort: Int by lazy {
        container.getMappedPort(5672)
    }
    val config by lazy {
        RabbitConfig(
            port = rabbitMqTestPort
        )
    }
    val orderProcessor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = RabbitExchangeConfiguration(
                keyIn = "in-v1",
                keyOut = "out-v1",
                exchange = exchange,
                queue = "v1-queue",
                consumerTag = "test-tag",
                exchangeType = exchangeType
            )
        )
    }
    val controller by lazy {
        OrderController(
            processors = setOf(orderProcessor)
        )
    }

    @BeforeTest
    fun init() {
        controller.start()
    }

    @Test
    fun orderCreateTest() {
        val keyOut = orderProcessor.processorConfig.keyOut
        val keyIn = orderProcessor.processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiObjectMapper.writeValueAsBytes(orderCreate))

                runBlocking {
                    withTimeoutOrNull(265L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiObjectMapper.readValue(responseJson, OrderCreateResponse::class.java)
                val expected = OrderStub.get()

                assertEquals(expected.walletId.asString(), response.order?.walletId)
                assertEquals(expected.type.name, response.order?.type?.name)
                assertEquals(expected.operation.name, response.order?.operation?.name)
            }
        }
    }

    private val orderCreate = with(OrderStub.get()) {
        OrderCreateRequest(
            order = OrderCreateObject(
                walletId = "wallet-1",
                amount = BigDecimal(10),
                type = OrderType.MARKET,
                operation = OrderOperation.BUYING
            ),
            requestType = "create",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            )
        )
    }
}
