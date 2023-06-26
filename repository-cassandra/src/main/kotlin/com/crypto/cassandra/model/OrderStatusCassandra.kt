package com.crypto.cassandra.model

import com.crypto.common.models.CommonOrderStatus

enum class OrderStatusCassandra {
    OPEN,
    CLOSED
}

fun OrderStatusCassandra?.fromTransport() = when(this) {
    null -> CommonOrderStatus.NONE
    OrderStatusCassandra.OPEN -> CommonOrderStatus.OPEN
    OrderStatusCassandra.CLOSED -> CommonOrderStatus.CLOSED
}

fun CommonOrderStatus.toTransport() = when(this) {
    CommonOrderStatus.NONE -> null
    CommonOrderStatus.OPEN -> OrderStatusCassandra.OPEN
    CommonOrderStatus.CLOSED -> OrderStatusCassandra.CLOSED
}