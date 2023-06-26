package com.crypto.cassandra.model

import com.crypto.common.models.CommonOrderType

enum class OrderTypeCassandra {
    MARKET,
    LIMIT
}

fun OrderTypeCassandra?.fromTransport() = when(this) {
    null -> CommonOrderType.NONE
    OrderTypeCassandra.MARKET -> CommonOrderType.MARKET
    OrderTypeCassandra.LIMIT -> CommonOrderType.LIMIT
}

fun CommonOrderType.toTransport() = when(this) {
    CommonOrderType.NONE -> null
    CommonOrderType.MARKET -> OrderTypeCassandra.MARKET
    CommonOrderType.LIMIT -> OrderTypeCassandra.LIMIT
}