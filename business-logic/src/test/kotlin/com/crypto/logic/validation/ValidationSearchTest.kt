package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.models.CommonCommand
import com.crypto.common.models.CommonOrderFilter
import com.crypto.common.models.CommonState
import com.crypto.common.models.CommonWorkMode
import com.crypto.logic.OrderProcessor
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ValidationSearchTest {

    private val command = CommonCommand.SEARCH
    private val processor by lazy { OrderProcessor() }

    @Test
    fun correctEmpty() = runBlocking {
        val ctx = CommonContext(
            command = command,
            state = CommonState.NONE,
            workMode = CommonWorkMode.TEST,
            orderFilterRequest = CommonOrderFilter()
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertNotEquals(CommonState.FAILING, ctx.state)
    }
}

