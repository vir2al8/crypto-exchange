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
abstract class RepositoryOrderDeleteTest {
    abstract val repository: OrderRepository

    protected open val deleteSuccess = initObjects[0]
    private val notFoundId = CommonOrderId("order-repository-delete-notFound")

    @Test
    fun deleteSuccess() = runRepositoryTest {
        val result = repository.deleteOrder(DbOrderIdRequest(deleteSuccess.id))

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepositoryTest {
        val result = repository.readOrder(DbOrderIdRequest(notFoundId))

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitOrders("delete") {
        override val initObjects: List<CommonOrder> = listOf(
            createInitTestModel("delete")
        )
    }
}