package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.CommonSettings
import com.crypto.common.models.*
import com.crypto.common.repository.DbOrderResponse
import com.crypto.common.repository.DbOrdersResponse
import com.crypto.logic.OrderProcessor
import com.crypto.repotest.OrderRepositoryMock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class OrderRepositorySearchTest {
    private val command = CommonCommand.SEARCH
    private val orderId = "10000000-0000-0000-0000-000000000001"
    private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
    private val repository = OrderRepositoryMock(
        invokeSearchOrder = {
            DbOrdersResponse(
                isSuccess = true,
                data = listOf(
                    CommonOrder(
                        id = CommonOrderId(orderId),
                        walletId = CommonWalletId("1011"),
                        amount = BigDecimal.TEN,
                        type = it.type,
                        operation = it.operation,
                        status = it.status,
                        createdAt = createdAt
                    )
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
    fun repositorySearchSuccessTest() = runTest {
        val context = CommonContext(
            command = command,
            state = CommonState.NONE,
            workMode = CommonWorkMode.TEST,
            orderFilterRequest = CommonOrderFilter(
                type = CommonOrderType.LIMIT,
                operation = CommonOrderOperation.SELLING,
                status = CommonOrderStatus.CLOSED
            ),
        )

        processor.exec(context)

        assertEquals(CommonState.FINISHING, context.state)
        assertNotEquals(CommonOrderId.NONE, context.ordersResponse.first().id)
        assertEquals(CommonWalletId("1011"), context.ordersResponse.first().walletId)
        assertEquals(BigDecimal.TEN, context.ordersResponse.first().amount)
        assertEquals(CommonOrderType.LIMIT, context.ordersResponse.first().type)
        assertEquals(CommonOrderOperation.SELLING, context.ordersResponse.first().operation)
        assertEquals(CommonOrderStatus.CLOSED, context.ordersResponse.first().status)
        assertEquals(Instant.parse("2023-03-03T08:05:57Z"), context.ordersResponse.first().createdAt)
    }

}