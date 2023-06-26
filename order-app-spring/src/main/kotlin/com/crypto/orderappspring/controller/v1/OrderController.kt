package com.crypto.orderappspring.controller.v1

import com.crypto.api.v1.models.*
import com.crypto.common.CommonContext
import com.crypto.mappers.*
import com.crypto.orderappspring.service.OrderBlockingProcessor
import com.crypto.stubs.OrderStub
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/order")
class OrderController(
    private val processor: OrderBlockingProcessor
) {

    @PostMapping("create")
    fun createOrder(@RequestBody request: OrderCreateRequest): OrderCreateResponse {
        val context = CommonContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportCreate()
    }

    @PostMapping("read")
    fun readOrder(@RequestBody request: OrderReadRequest): OrderReadResponse {
        val context = CommonContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportRead()
    }

    @PostMapping("delete")
    fun deleteOrder(@RequestBody request: OrderDeleteRequest): OrderDeleteResponse {
        val context = CommonContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportDelete()
    }

    @PostMapping("search")
    fun searchOrder(@RequestBody request: OrderSearchRequest): OrderSearchResponse {
        val context = CommonContext()
        context.fromTransport(request)
        processor.exec(context)
        return context.toTransportSearch()
    }
}