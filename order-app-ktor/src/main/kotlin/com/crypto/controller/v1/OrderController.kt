package com.crypto.controller.v1

import com.crypto.api.v1.models.OrderCreateRequest
import com.crypto.api.v1.models.OrderDeleteRequest
import com.crypto.api.v1.models.OrderReadRequest
import com.crypto.api.v1.models.OrderSearchRequest
import com.crypto.common.CommonContext
import com.crypto.configs.OrderAppConfigs
import com.crypto.mappers.*
import com.crypto.stubs.OrderStub
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

suspend fun ApplicationCall.createOrder(appConfigs: OrderAppConfigs) {
    val request = receive<OrderCreateRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    appConfigs.processor.exec(context)
    respond(context.toTransportCreate())
}

suspend fun ApplicationCall.readOrder(appConfigs: OrderAppConfigs) {
    val request = receive<OrderReadRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    appConfigs.processor.exec(context)
    respond(context.toTransportRead())
}

suspend fun ApplicationCall.deleteOrder(appConfigs: OrderAppConfigs) {
    val request = receive<OrderDeleteRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    appConfigs.processor.exec(context)
    respond(HttpStatusCode.NoContent, context.toTransportDelete())
}

suspend fun ApplicationCall.searchOrder(appConfigs: OrderAppConfigs) {
    val request = receive<OrderSearchRequest>()
    val context = CommonContext()
    context.fromTransport(request)
    appConfigs.processor.exec(context)
    respond(context.toTransportSearch())
}