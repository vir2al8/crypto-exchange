package com.crypto.repotest

import com.crypto.common.models.*
import com.crypto.common.repository.DbOrderIdRequest
import com.crypto.common.repository.DbOrderRequest
import com.crypto.common.repository.OrderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryOrderReadTest {
    abstract val repository: OrderRepository
    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepositoryTest {
        val result = repository.readOrder(DbOrderIdRequest(readSuccess.id))

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccess, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepositoryTest {
        val result = repository.readOrder(DbOrderIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitOrders("read") {
        override val initObjects: List<CommonOrder> = listOf(
            createInitTestModel("read")
        )
        val notFoundId = CommonOrderId("order-repository-read-notFound")
    }
}