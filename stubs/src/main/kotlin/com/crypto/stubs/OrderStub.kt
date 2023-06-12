package com.crypto.stubs

import com.crypto.common.models.CommonOrder
import com.crypto.common.models.CommonOrderId
import com.crypto.common.models.CommonOrderOperation

object OrderStub {
    fun get(): CommonOrder = OrderStubUsdt.ORDER_BUYING_USDT.copy()

    fun prepareResult(block: CommonOrder.() -> Unit): CommonOrder = get().apply(block)
    fun prepareSearchList(operation: CommonOrderOperation) = listOf(
        commonOrderOperation(id = "order-1", operation),
        commonOrderOperation(id = "order-2", operation),
        commonOrderOperation(id = "order-3", operation),
        commonOrderOperation(id = "order-4", operation),
        commonOrderOperation(id = "order-5", operation),
    )

    private fun commonOrderOperation(id: String, operation: CommonOrderOperation) =
        commonOrder(get(), id = id, operation = operation)

    private fun commonOrder(
        base: CommonOrder,
        id: String,
        operation: CommonOrderOperation
    ) = base.copy(
        id = CommonOrderId(id),
        operation = operation
    )
}
