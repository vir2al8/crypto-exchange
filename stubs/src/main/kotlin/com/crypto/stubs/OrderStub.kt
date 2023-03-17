package com.crypto.stubs

import com.crypto.common.models.CommonOrder

object OrderStub {
    fun get(): CommonOrder = OrderStubUsdt.ORDER_BUYING_USDT.copy()
}