package com.crypto.repotest

import com.crypto.common.repository.*

class OrderRepositoryMock(
    private val invokeCreateOrder: (DbOrderRequest) -> DbOrderResponse = { DbOrderResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadOrder: (DbOrderIdRequest) -> DbOrderResponse = { DbOrderResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteOrder: (DbOrderIdRequest) -> DbOrderResponse = { DbOrderResponse.MOCK_SUCCESS_EMPTY },
    private val invokeSearchOrder: (DbOrderFilterRequest) -> DbOrdersResponse = { DbOrdersResponse.MOCK_SUCCESS_EMPTY },
): OrderRepository {
    override suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse {
        return invokeCreateOrder(rq)
    }
    override suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse {
        return invokeReadOrder(rq)
    }
    override suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse {
        return invokeDeleteOrder(rq)
    }
    override suspend fun searchOrder(rq: DbOrderFilterRequest): DbOrdersResponse {
        return invokeSearchOrder(rq)
    }
}