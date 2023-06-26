package com.crypto.repostub

import com.crypto.common.models.CommonOrderOperation
import com.crypto.common.repository.*
import com.crypto.stubs.OrderStub

class OrderRepositoryStub : OrderRepository {
    override suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse {
        return DbOrderResponse(
            data = OrderStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse {
        return DbOrderResponse(
            data = OrderStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse {
        return DbOrderResponse(
            data = OrderStub.prepareResult {  },
            isSuccess = true
        )
    }

    override suspend fun searchOrder(rq: DbOrderFilterRequest): DbOrdersResponse {
        return DbOrdersResponse(
            data = OrderStub.prepareSearchList(operation = CommonOrderOperation.BUYING),
            isSuccess = true
        )
    }
}