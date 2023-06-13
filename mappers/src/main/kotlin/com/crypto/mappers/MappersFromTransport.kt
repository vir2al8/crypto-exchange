package com.crypto.mappers

import com.crypto.api.v1.models.*
import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.common.stubs.CommonStub
import com.crypto.mappers.exceptions.UnknownRequestException
import java.math.BigDecimal

fun CommonContext.fromTransport(request: IRequest) = when (request) {
    is OrderCreateRequest -> fromTransport(request)
    is OrderDeleteRequest -> fromTransport(request)
    is OrderReadRequest -> fromTransport(request)
    is OrderSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestException(request::class)
}

fun IRequest?.requestId() = this?.requestId?.let { CommonRequestId(it) } ?: CommonRequestId.NONE
fun String?.toWalletId() = this?.let { CommonWalletId(it) } ?: CommonWalletId.NONE
fun String?.toOrderId() = this?.let { CommonOrderId(it) } ?: CommonOrderId.NONE
fun String?.toOrderWithId() = CommonOrder(id = this.toOrderId())

fun CommonContext.fromTransport(request: OrderCreateRequest) {
    command = CommonCommand.CREATE
    requestId = request.requestId()
    orderRequest = request.order?.toInternal() ?: CommonOrder()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CommonContext.fromTransport(request: OrderDeleteRequest) {
    command = CommonCommand.CREATE
    requestId = request.requestId()
    orderRequest = request.order?.id.toOrderWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CommonContext.fromTransport(request: OrderReadRequest) {
    command = CommonCommand.READ
    requestId = request.requestId()
    orderRequest = request.order?.id.toOrderWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun CommonContext.fromTransport(request: OrderSearchRequest) {
    command = CommonCommand.SEARCH
    requestId = request.requestId()
    orderFilterRequest = request.orderFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun OrderCreateObject.toInternal() = CommonOrder(
    walletId = this.walletId.toWalletId(),
    amount = this.amount ?: BigDecimal.ZERO,
    type = this.type.fromTransport(),
    operation = this.operation.fromTransport(),
)

fun OrderSearchFilter?.toInternal() = CommonOrderFilter(
    type = this?.type.fromTransport(),
    operation = this?.operation.fromTransport(),
    status = this?.status.fromTransport(),
)

fun OrderStatus?.fromTransport() = when (this) {
    OrderStatus.OPEN -> CommonOrderStatus.OPEN
    OrderStatus.CLOSED -> CommonOrderStatus.CLOSED
    null -> CommonOrderStatus.NONE
}

fun OrderDebug?.transportToStubCase() = when (this?.stub) {
    OrderRequestDebugStubs.SUCCESS -> CommonStub.SUCCESS
    OrderRequestDebugStubs.NOT_FOUND -> CommonStub.NOT_FOUND
    OrderRequestDebugStubs.BAD_ID -> CommonStub.BAD_ID
    OrderRequestDebugStubs.BAD_TYPE -> CommonStub.BAD_TYPE
    OrderRequestDebugStubs.BAD_OPERATION -> CommonStub.BAD_OPERATION
    OrderRequestDebugStubs.BAD_WALLET_ID -> CommonStub.BAD_WALLET_ID
    OrderRequestDebugStubs.DB_ERROR -> CommonStub.DB_ERROR
    null -> CommonStub.NONE
}

fun OrderDebug?.transportToWorkMode() = when (this?.mode) {
    OrderRequestDebugMode.PROD -> CommonWorkMode.PROD
    OrderRequestDebugMode.TEST -> CommonWorkMode.TEST
    OrderRequestDebugMode.STUB -> CommonWorkMode.STUB
    null -> CommonWorkMode.PROD
}

fun OrderType?.fromTransport() = when (this) {
    OrderType.MARKET -> CommonOrderType.MARKET
    OrderType.LIMIT -> CommonOrderType.LIMIT
    null -> CommonOrderType.NONE
}

fun OrderOperation?.fromTransport() = when (this) {
    OrderOperation.BUYING -> CommonOrderOperation.BUYING
    OrderOperation.SELLING -> CommonOrderOperation.SELLING
    null -> CommonOrderOperation.NONE
}