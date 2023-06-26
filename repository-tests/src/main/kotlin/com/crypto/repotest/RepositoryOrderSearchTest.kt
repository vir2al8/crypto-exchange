package com.crypto.repotest

import com.crypto.common.models.*
import com.crypto.common.repository.DbOrderFilterRequest
import com.crypto.common.repository.OrderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepositoryOrderSearchTest {
    abstract val repository: OrderRepository

    protected open val initializedObjects: List<CommonOrder> = initObjects

    @Test
    fun searchType() = runRepositoryTest {
        val result = repository.searchOrder(DbOrderFilterRequest(type = CommonOrderType.MARKET))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchOperation() = runRepositoryTest {
        val result = repository.searchOrder(DbOrderFilterRequest(operation = CommonOrderOperation.SELLING))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchStatus() = runRepositoryTest {
        val result = repository.searchOrder(DbOrderFilterRequest(status = CommonOrderStatus.CLOSED))
        assertEquals(true, result.isSuccess)
        val expected = listOf(initializedObjects[5], initializedObjects[6]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data?.sortedBy { it.id.asString() })
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitOrders("search") {
        override val initObjects: List<CommonOrder> = listOf(
            createInitTestModel("order1"),
            createInitTestModel("order2", orderType = CommonOrderType.MARKET),
            createInitTestModel("order3", operation = CommonOrderOperation.SELLING),
            createInitTestModel("order4", orderType = CommonOrderType.MARKET),
            createInitTestModel("order5", operation = CommonOrderOperation.SELLING),
            createInitTestModel("order6", status = CommonOrderStatus.CLOSED),
            createInitTestModel("order7", status = CommonOrderStatus.CLOSED),
        )
    }
}