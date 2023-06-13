package com.crypto.processor

import com.crypto.api.apiObjectMapper
import com.crypto.api.v1.models.IRequest
import com.crypto.common.CommonContext
import com.crypto.common.models.CommonState
import com.crypto.configs.RabbitConfig
import com.crypto.configs.RabbitExchangeConfiguration
import com.crypto.mappers.fromTransport
import com.crypto.mappers.toTransport
import com.crypto.stubs.OrderStub
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import java.time.Instant

class CommonOrderProcessor { // TODO fix it
    fun exec(context: CommonContext) {
        context.orderResponse = OrderStub.get()
    }
}

class RabbitDirectProcessor(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val processor: CommonOrderProcessor = CommonOrderProcessor(),
) : RabbitProcessorBase(config, processorConfig) {

    private val context = CommonContext()

    override suspend fun Channel.processMessage(message: Delivery) {
        context.apply {
            timeStart = Instant.now()
        }

        apiObjectMapper.readValue(message.body, IRequest::class.java).run {
            context.fromTransport(this).also {
                println("TYPE: ${this::class.simpleName}")
            }
        }
        val response = processor.exec(context).run { context.toTransport() }
        apiObjectMapper.writeValueAsBytes(response).also {
            println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }.also {
            println("published")
        }
    }

    override fun Channel.onError(e: Throwable) {
        e.printStackTrace()
        context.state = CommonState.FAILING
//        context.addError(error = arrayOf(e.asCommonError()))
        val response = context.toTransport()
        apiObjectMapper.writeValueAsBytes(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it)
        }
    }
}
