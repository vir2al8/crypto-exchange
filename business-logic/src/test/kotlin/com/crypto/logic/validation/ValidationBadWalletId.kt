package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.common.permissions.CommonPrincipalModel
import com.crypto.common.permissions.CommonUserGroups
import com.crypto.logic.OrderProcessor
import com.crypto.stubs.OrderStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = OrderStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationWalletIdCorrect(command: CommonCommand, processor: OrderProcessor) = runTest {
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
        principal = CommonPrincipalModel(
            id = stub.walletId,
            groups = setOf(
                CommonUserGroups.USER,
                CommonUserGroups.TEST,
            )
        ),
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommonState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationWalletIdTrim(command: CommonCommand, processor: OrderProcessor) = runTest {
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
        principal = CommonPrincipalModel(
            id = stub.walletId,
            groups = setOf(
                CommonUserGroups.USER,
                CommonUserGroups.TEST,
            )
        ),
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(CommonState.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationWalletIdEmpty(command: CommonCommand, processor: OrderProcessor) = runTest {
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

@OptIn(ExperimentalCoroutinesApi::class)
fun validationWalletIdFormat(command: CommonCommand, processor: OrderProcessor) = runTest {
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
