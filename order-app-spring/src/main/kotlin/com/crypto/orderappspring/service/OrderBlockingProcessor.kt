package com.crypto.orderappspring.service

import com.crypto.common.CommonContext
import com.crypto.logic.OrderProcessor
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class OrderBlockingProcessor {
    private val processor = OrderProcessor()

    fun exec(ctx: CommonContext) = runBlocking { processor.exec(ctx) }
}