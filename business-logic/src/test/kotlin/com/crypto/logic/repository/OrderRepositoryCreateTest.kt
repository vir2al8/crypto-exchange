package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.CommonSettings
import com.crypto.common.models.*
import com.crypto.common.repository.DbOrderResponse
import com.crypto.logic.OrderProcessor
import com.crypto.repotest.OrderRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class OrderRepositoryCreateTest {
    private val command = CommonCommand.CREATE
    private val orderId = "10000000-0000-0000-0000-000000000001"
    private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
    private val repository = OrderRepositoryMock(
        invokeCreateOrder = {
            DbOrderResponse(
                isSuccess = true,
                data = CommonOrder(
                    id = CommonOrderId(orderId),
                    walletId = it.order.walletId,
                    amount = it.order.amount,
                    type = it.order.type,
                    operation = it.order.operation,
                    status = it.order.status,
                    createdAt = createdAt
                )
            )
        }
    )
    private val settings = CommonSettings(
        repositoryTest = repository
    )
    private val processor = OrderProcessor(settings)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun repositoryCreateSuccessTest() = runTest {
        val context = CommonContext(
            command = command,
            state = CommonState.NONE,
            workMode = CommonWorkMode.TEST,
            orderRequest = CommonOrder(
                walletId = CommonWalletId("1011"),
                amount = BigDecimal.TEN,
                type = CommonOrderType.MARKET,
                operation = CommonOrderOperation.BUYING,
                status = CommonOrderStatus.OPEN,
            ),
        )

        processor.exec(context)

        assertEquals(CommonState.FINISHING, context.state)
        assertNotEquals(CommonOrderId.NONE, context.orderResponse.id)
        assertEquals(CommonWalletId("1011"), context.orderResponse.walletId)
        assertEquals(BigDecimal.TEN, context.orderResponse.amount)
        assertEquals(CommonOrderType.MARKET, context.orderResponse.type)
        assertEquals(CommonOrderOperation.BUYING, context.orderResponse.operation)
        assertEquals(CommonOrderStatus.OPEN, context.orderResponse.status)
        assertEquals(Instant.parse("2023-03-03T08:05:57Z"), context.orderResponse.createdAt)
    }
}