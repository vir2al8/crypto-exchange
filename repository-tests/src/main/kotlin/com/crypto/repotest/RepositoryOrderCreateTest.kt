package com.crypto.repotest

import com.crypto.common.models.*
import com.crypto.common.repository.DbOrderRequest
import com.crypto.common.repository.OrderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryOrderCreateTest {
    abstract val repository: OrderRepository

    private val createObject = CommonOrder(
        walletId = CommonWalletId("1011"),
        amount = BigDecimal.TEN,
        type = CommonOrderType.MARKET,
        operation = CommonOrderOperation.BUYING,
        status = CommonOrderStatus.OPEN,
        createdAt = Instant.parse("2023-03-03T08:05:57Z")
    )

    @Test
    fun createSuccess() = runRepositoryTest {


        val result = repository.createOrder(DbOrderRequest(createObject))

        val expected = createObject.copy(id = result.data?.id ?: CommonOrderId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.amount, result.data?.amount)
        assertEquals(expected.walletId, result.data?.walletId)
        assertEquals(expected.type, result.data?.type)
        assertEquals(expected.operation, result.data?.operation)
        assertEquals(expected.status, result.data?.status)
        assertNotEquals(CommonOrderId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrders("create") {
        override val initObjects: List<CommonOrder> = emptyList()
    }
}