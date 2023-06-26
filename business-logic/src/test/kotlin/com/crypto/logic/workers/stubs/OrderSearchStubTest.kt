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
import kotlin.test.assertTrue
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
class OrderSearchStubTest {
    private val processor = OrderProcessor()
    val operation = CommonOrderOperation.BUYING

    @Test
    fun read() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.SEARCH,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.SUCCESS,
            orderFilterRequest = CommonOrderFilter(
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
    fun databaseError() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.SEARCH,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.DB_ERROR,
            orderFilterRequest = CommonOrderFilter(
                operation = operation
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badOperation() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.CREATE,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.BAD_OPERATION,
            orderFilterRequest = CommonOrderFilter(
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("operation", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = CommonContext(
            command = CommonCommand.SEARCH,
            state = CommonState.NONE,
            workMode = CommonWorkMode.STUB,
            stubCase = CommonStub.NONE,
            orderFilterRequest = CommonOrderFilter(
                operation = operation
            ),
        )
        processor.exec(ctx)
        assertEquals(CommonOrder(), ctx.orderResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
