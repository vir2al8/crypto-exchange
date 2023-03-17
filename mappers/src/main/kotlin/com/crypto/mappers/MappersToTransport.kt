package com.crypto.mappers

import com.crypto.api.v1.models.*
import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.mappers.exceptions.UnknownCommandException

fun CommonContext.toTransport(): IResponse = when (command) {
    CommonCommand.CREATE -> toTransportCreate()
    CommonCommand.READ -> toTransportRead()
    CommonCommand.DELETE -> toTransportDelete()
    CommonCommand.SEARCH -> toTransportSearch()
    CommonCommand.NONE -> throw UnknownCommandException(command)
}

fun CommonContext.toTransportCreate() = OrderCreateResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CommonState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder(),
)

fun CommonContext.toTransportRead() = OrderReadResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CommonState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder(),
)

fun CommonContext.toTransportDelete() = OrderDeleteResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CommonState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    order = orderResponse.toTransportOrder(),
)

fun CommonContext.toTransportSearch() = OrderSearchResponse(
    requestId = requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == CommonState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    orders = ordersResponse.toTransportOrders()
)

fun MutableList<CommonOrder>.toTransportOrders() = this
    .map { it.toTransportOrder() }
    .toList()
    .takeIf { isNotEmpty() }

fun CommonOrder.toTransportOrder() = OrderResponseObject(
    id = id.takeIf { it != CommonOrderId.NONE }?.asString(),
    walletId = walletId.takeIf { it != CommonWalletId.NONE }?.asString(),
    amount = amount,
    type = type.toTransportOrderType(),
    operation = operation.toTransportOrderOperation(),
    status = status.toTransportOrderStatus(),
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)

private fun CommonOrderStatus.toTransportOrderStatus() = when(this) {
    CommonOrderStatus.OPEN -> OrderStatus.OPEN
    CommonOrderStatus.CLOSED -> OrderStatus.CLOSED
    CommonOrderStatus.NONE -> null
}

private fun CommonOrderOperation.toTransportOrderOperation() = when(this) {
    CommonOrderOperation.BUYING -> OrderOperation.BUYING
    CommonOrderOperation.SELLING -> OrderOperation.SELLING
    CommonOrderOperation.NONE -> null
}

private fun CommonOrderType.toTransportOrderType() = when(this) {
    CommonOrderType.MARKET -> OrderType.MARKET
    CommonOrderType.LIMIT -> OrderType.LIMIT
    CommonOrderType.NONE -> null
}

fun MutableList<CommonError>.toTransportErrors() = map(::toTransportError).toList().takeIf { it.isNotEmpty() }

fun toTransportError(commonError: CommonError) = Error(
    code = commonError.code.takeIf { it.isNotBlank() },
    group = commonError.group.takeIf { it.isNotBlank() },
    field = commonError.field.takeIf { it.isNotBlank() },
    message = commonError.message.takeIf { it.isNotBlank() },
)