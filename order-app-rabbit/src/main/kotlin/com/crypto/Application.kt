package com.crypto

import com.crypto.configs.RabbitConfig
import com.crypto.configs.RabbitExchangeConfiguration
import com.crypto.controller.OrderController
import com.crypto.processor.CommonOrderProcessor
import com.crypto.processor.RabbitDirectProcessor

fun main() {
    val config = RabbitConfig()
    val orderProcessor = CommonOrderProcessor()

    val orderProducerConfig = RabbitExchangeConfiguration(
        keyIn = "order-app-in",
        keyOut = "order-app-out",
        exchange = "transport-exchange",
        queue = "queue",
        consumerTag = "consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = orderProducerConfig,
            processor = orderProcessor
        )
    }

    val controller by lazy {
        OrderController(
            processors = setOf(processor)
        )
    }
    controller.start()
}
