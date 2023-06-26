package com.crypto.logic.workers.stubs

import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.common.stubs.CommonStub
import com.crypto.logic.OrderProcessor
import com.crypto.stubs.OrderStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class OrderCreateStubTest {
    private val processor = OrderProcessor()
    val id = CommonOrderId("order-1")
    val walletId = CommonWalletId("wallet-id-1")
    val type = CommonOrderType.LIMIT
    val operation = CommonOrderOperation.BUYING

    @Test
    fun create() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.SUCCESS,
            orderRequest = CommonOrder(
                walletId = walletId,
                type = type,
                operation = operation,
            ),
        )
        processor.exec(ctx)
        assertEquals(OrderStub.get().id, ctx.orderResponse.id)
        assertEquals(walletId, ctx.orderResponse.walletId)
        assertEquals(type, ctx.orderResponse.type)
        assertEquals(operation, ctx.orderResponse.operation)
    }

    @Test
    fun badWalletId() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.BAD_WALLET_ID,
            orderRequest = CommonOrder(
                type = type,
                operation = operation,
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("wallet-id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badType() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.BAD_TYPE,
            orderRequest = CommonOrder(
                walletId = walletId,
                operation = operation,
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("type", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badOperation() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.BAD_OPERATION,
            orderRequest = CommonOrder(
                walletId = walletId,
                type = type,
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("operation", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.DB_ERROR,
            orderRequest = CommonOrder(
                walletId = walletId,
                type = type,
                operation = operation,
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.BAD_ID,
            orderRequest = CommonOrder(
                id = id,
                walletId = walletId,
                type = type,
                operation = operation,
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
