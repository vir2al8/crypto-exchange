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
import kotlin.test.assertEquals

private val command = CommonCommand.READ
private val orderId = "10000000-0000-0000-0000-000000000001"
private val createdAt = Instant.parse("2023-03-03T08:05:57Z");
private val initOrder = CommonOrder(
    id = CommonOrderId(orderId),
    walletId = CommonWalletId("1011"),
    amount = BigDecimal.TEN,
    type = CommonOrderType.MARKET,
    operation = CommonOrderOperation.BUYING,
    status = CommonOrderStatus.OPEN,
    createdAt = createdAt
)
private val repository = OrderRepositoryMock(
    invokeReadOrder = {
        if (it.id == initOrder.id) {
            DbOrderResponse(
                isSuccess = true,
                data = initOrder
            )
        } else DbOrderResponse(
            isSuccess = false,
            data = null,
            errors = listOf(CommonError(message = "Not found", field = "id"))
        )
    }
)
private val settings = CommonSettings(
    repositoryTest = repository
)
private val processor = OrderProcessor(settings)

@OptIn(ExperimentalCoroutinesApi::class)
fun repositoryNotFoundTest(command: CommonCommand) = runTest {
    val context = CommonContext(
        command = command,
        state = CommonState.NONE,
        workMode = CommonWorkMode.TEST,
        orderRequest = CommonOrder(
            id = CommonOrderId("12345"),
            walletId = CommonWalletId("1011"),
            amount = BigDecimal.TEN,
            type = CommonOrderType.MARKET,
            operation = CommonOrderOperation.BUYING,
            status = CommonOrderStatus.OPEN,
        ),
    )

    processor.exec(context)

    assertEquals(CommonState.FAILING, context.state)
    assertEquals(CommonOrder(), context.orderResponse)
    assertEquals(1, context.errors.size)
    assertEquals("id", context.errors.first().field)
}