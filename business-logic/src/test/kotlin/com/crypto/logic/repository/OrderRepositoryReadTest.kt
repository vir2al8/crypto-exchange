package com.crypto.logic.repository

import com.crypto.common.CommonContext
import com.crypto.common.CommonSettings
import com.crypto.common.models.*
import com.crypto.common.permissions.CommonPrincipalModel
import com.crypto.common.permissions.CommonUserGroups
import com.crypto.common.repository.DbOrderResponse
import com.crypto.logic.OrderProcessor
import com.crypto.repotest.OrderRepositoryMock
import com.crypto.stubs.OrderStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = OrderStub.get()

class OrderRepositoryReadTest {
    private val command = CommonCommand.READ
    private val orderId = "10000000-0000-0000-0000-000000000001"
    private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
    private val repository = OrderRepositoryMock(
        invokeReadOrder = {
            DbOrderResponse(
                isSuccess = true,
                data = CommonOrder(
                    id = CommonOrderId(orderId),
                    walletId = CommonWalletId("1011"),
                    amount = BigDecimal.TEN,
                    type = CommonOrderType.MARKET,
                    operation = CommonOrderOperation.BUYING,
                    status = CommonOrderStatus.OPEN,
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
    fun repositoryReadSuccessTest() = runTest {
        val context = CommonContext(
            command = command,
            state = CommonState.NONE,
            workMode = CommonWorkMode.TEST,
            orderRequest = CommonOrder(
                id = CommonOrderId(orderId),
            ),
            principal = CommonPrincipalModel(
                id = stub.walletId,
                groups = setOf(
                    CommonUserGroups.USER,
                    CommonUserGroups.TEST,
                )
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

    @Test
    fun repoReadNotFoundTest() = repositoryNotFoundTest(command)
}