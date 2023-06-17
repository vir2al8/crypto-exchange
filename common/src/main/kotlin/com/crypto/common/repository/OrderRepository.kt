package com.crypto.common.repository

interface OrderRepository {
    suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse
    suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse
    suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse
    suspend fun searchOrder(rq: DbOrderFilterRequest): DbOrdersResponse
    companion object {
        val NONE = object : OrderRepository {
            override suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse {
                TODO("Not yet implemented")
            }
            override suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse {
                TODO("Not yet implemented")
            }
            override suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse {
                TODO("Not yet implemented")
            }
            override suspend fun searchOrder(rq: DbOrderFilterRequest): DbOrdersResponse {
                TODO("Not yet implemented")
            }
        }
    }
}