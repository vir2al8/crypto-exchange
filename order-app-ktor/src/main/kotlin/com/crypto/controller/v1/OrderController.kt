package com.crypto.controller.v1

import com.crypto.api.v1.models.OrderCreateRequest
import com.crypto.api.v1.models.OrderDeleteRequest
import com.crypto.api.v1.models.OrderReadRequest
import com.crypto.api.v1.models.OrderSearchRequest
import com.crypto.common.CommonContext
import com.crypto.mappers.*
import com.crypto.stubs.OrderStub
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.createOrder() {
    val request = receive<OrderCreateRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    context.orderResponse = OrderStub.get()
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.readOrder() {
    val request = receive<OrderReadRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    context.orderResponse = OrderStub.get()
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.deleteOrder() {
    val request = receive<OrderDeleteRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    respond(HttpStatusCode.NoContent, context.toTransportDelete())
}

suspend fun ApplicationCall.searchOrder() {
    val request = receive<OrderSearchRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    context.ordersResponse.add(OrderStub.get())
    respond(context.toTransportSearch())
}