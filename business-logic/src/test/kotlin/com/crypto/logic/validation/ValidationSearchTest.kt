package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.CommonSettings
import com.crypto.common.models.CommonCommand
import com.crypto.common.models.CommonOrderFilter
import com.crypto.common.models.CommonState
import com.crypto.common.models.CommonWorkMode
import com.crypto.logic.OrderProcessor
import com.crypto.repostub.OrderRepositoryStub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ValidationSearchTest {
    private val settings by lazy {
        CommonSettings(
            repoTest = OrderRepositoryStub()
        )
    }

    private val command = CommonCommand.SEARCH
    private val processor by lazy { OrderProcessor(settings) }

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