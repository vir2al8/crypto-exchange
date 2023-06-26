package com.crypto.cassandra.model

import com.crypto.common.models.CommonOrderOperation

enum class OrderOperationCassandra {
    BUYING,
    SELLING
}

fun OrderOperationCassandra?.fromTransport() = when(this) {
    null -> CommonOrderOperation.NONE
    OrderOperationCassandra.BUYING -> CommonOrderOperation.BUYING
    OrderOperationCassandra.SELLING -> CommonOrderOperation.SELLING
}

fun CommonOrderOperation.toTransport() = when(this) {
    CommonOrderOperation.NONE -> null
    CommonOrderOperation.BUYING -> OrderOperationCassandra.BUYING
    CommonOrderOperation.SELLING -> OrderOperationCassandra.SELLING
}