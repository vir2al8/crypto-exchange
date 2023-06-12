package ru.otus.otuskotlin.marketplace.biz.validation.stub

import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.common.stubs.CommonStub
import com.crypto.logic.OrderProcessor
import com.crypto.stubs.OrderStub
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class OrderSearchStubTest {
    private val processor = OrderProcessor()
    val operation = CommonOrderOperation.BUYING

    @Test
    fun read() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.SEARCH,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.SUCCESS,
            orderFilterRequest = CommonFilterOrder(
                operation = operation
            ),
        )
        processor.exec(ctx)
        val first = ctx.ordersResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.operation.equals(ctx.orderFilterRequest.operation))
        with (OrderStub.get()) {
            assertEquals(operation, first.operation)
        }
    }

    @Test
    fun databaseError() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.SEARCH,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.DB_ERROR,
            orderFilterRequest = CommonFilterOrder(
                operation = operation
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badOperation() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.BAD_OPERATION,
            orderFilterRequest = CommonFilterOrder(
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("operation", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runBlocking {
        val ctx = CommonContext(
            command = CommonCommand.SEARCH,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.NONE,
            orderFilterRequest = CommonFilterOrder(
                operation = operation
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
