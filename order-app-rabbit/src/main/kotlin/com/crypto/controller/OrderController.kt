package com.crypto.controller

import com.crypto.processor.RabbitProcessorBase
import kotlinx.coroutines.*

class OrderController(
    private val processors: Set<RabbitProcessorBase>
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val limitedParallelismContext = Dispatchers.IO.limitedParallelism(1)

    private val scope = CoroutineScope(
        limitedParallelismContext + CoroutineName("thread-rabbitmq-controller")
    )


    fun start() = scope.launch {
        processors.forEach {
            launch(
                limitedParallelismContext + CoroutineName("thread-${it.processorConfig.consumerTag}")
            ) {
                try {
                    it.process()
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
