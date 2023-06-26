package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.logic.OrderProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeCorrect(command: CommonCommand, processor: OrderProcessor) = runTest {
    val ctx = CommonContext(
        command = command,
        state = CommonState.NONE,
        workMode = CommonWorkMode.TEST,
        orderRequest = CommonOrder(
            id = CommonOrderId("123-234-abc-ABC"),
            walletId = CommonWalletId("123-234-abc-ABC"),
            type = CommonOrderType.LIMIT,
            operation = CommonOrderOperation.BUYING,
        ),
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommonState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTypeNone(command: CommonCommand, processor: OrderProcessor) = runTest {
    val ctx = CommonContext(
        command = command,
        state = CommonState.NONE,
        workMode = CommonWorkMode.TEST,
        orderRequest = CommonOrder(
            id = CommonOrderId("123-234-abc-ABC"),
            walletId = CommonWalletId("123-234-abc-ABC"),
            type = CommonOrderType.NONE,
            operation = CommonOrderOperation.BUYING,
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(CommonState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("type", error?.field)
    assertContains(error?.message ?: "", "type")
}