package com.crypto.logic.validation

import com.crypto.common.CommonContext
import com.crypto.common.CommonSettings
import com.crypto.common.models.CommonCommand
import com.crypto.common.models.CommonOrderFilter
import com.crypto.common.models.CommonState
import com.crypto.common.models.CommonWorkMode
import com.crypto.common.permissions.CommonPrincipalModel
import com.crypto.common.permissions.CommonUserGroups
import com.crypto.logic.OrderProcessor
import com.crypto.repostub.OrderRepositoryStub
import com.crypto.stubs.OrderStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val stub = OrderStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
class ValidationSearchTest {
    private val settings by lazy {
        CommonSettings(
            repositoryTest = OrderRepositoryStub()
        )
    }

    private val command = CommonCommand.SEARCH
    private val processor by lazy { OrderProcessor(settings) }

    @Test
    fun correctEmpty() = runTest {
        val ctx = CommonContext(
            command = command,
            state = CommonState.NONE,
            workMode = CommonWorkMode.TEST,
            orderFilterRequest = CommonOrderFilter(),
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
}