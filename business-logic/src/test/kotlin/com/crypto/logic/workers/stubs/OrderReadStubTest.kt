package ru.otus.otuskotlin.marketplace.biz.validation.stub

import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.common.stubs.CommonStub
import com.crypto.logic.OrderProcessor
import com.crypto.stubs.OrderStub
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class OrderReadStubTest {
    private val processor = OrderProcessor()
    val id = CommonOrderId("order-1")

    @Test
    fun read() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.READ,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.SUCCESS,
            orderRequest = CommonOrder(
                id = id
            ),
        )
        processor.exec(ctx)
        assertEquals(OrderStub.get().id, ctx.orderResponse.id)
    }

    @Test
    fun badId() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.READ,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.BAD_ID,
            orderRequest = CommonOrder(
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.READ,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.DB_ERROR,
            orderRequest = CommonOrder(
                id = id
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.READ,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.NONE,
            orderRequest = CommonOrder(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
