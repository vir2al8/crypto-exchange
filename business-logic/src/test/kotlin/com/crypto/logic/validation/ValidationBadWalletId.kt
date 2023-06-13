package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.logic.OrderProcessor
import com.crypto.stubs.OrderStub
import kotlinx.coroutines.runBlocking
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationWalletIdCorrect(command: CommonCommand, processor: OrderProcessor) = runBlocking {
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

fun validationWalletIdTrim(command: CommonCommand, processor: OrderProcessor) = runBlocking {
    val ctx = CommonContext(
        command = command,
        state = CommonState.NONE,
        workMode = CommonWorkMode.TEST,
        orderRequest = CommonOrder(
            id = CommonOrderId("123-234-abc-ABC"),
            walletId = CommonWalletId(" \n\t 123-234-abc-ABC \n\t "),
            type = CommonOrderType.LIMIT,
            operation = CommonOrderOperation.BUYING,
        ),
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommonState.FAILING, ctx.state)
}

fun validationWalletIdEmpty(command: CommonCommand, processor: OrderProcessor) = runBlocking {
    val ctx = CommonContext(
        command = command,
        state = CommonState.NONE,
        workMode = CommonWorkMode.TEST,
        orderRequest = CommonOrder(
            id = CommonOrderId("123-234-abc-ABC"),
            walletId = CommonWalletId(""),
            type = CommonOrderType.LIMIT,
            operation = CommonOrderOperation.BUYING,
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(CommonState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("walledId", error?.field)
    assertContains(error?.message ?: "", "walledId")
}

fun validationWalletIdFormat(command: CommonCommand, processor: OrderProcessor) = runBlocking {
    val ctx = CommonContext(
        command = command,
        state = CommonState.NONE,
        workMode = CommonWorkMode.TEST,
        orderRequest = CommonOrder(
            id = CommonOrderId("123-234-abc-ABC"),
            walletId = CommonWalletId("!@#\$%^&*(),.{}"),
            type = CommonOrderType.LIMIT,
            operation = CommonOrderOperation.BUYING,
        ),
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(CommonState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("walletId", error?.field)
    assertContains(error?.message ?: "", "walletId")
}
