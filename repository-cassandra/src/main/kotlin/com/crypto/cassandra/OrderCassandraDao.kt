package com.crypto.cassandra

import com.crypto.cassandra.model.OrderCassandraDto
import com.crypto.common.repository.DbOrderFilterRequest
import com.datastax.oss.driver.api.mapper.annotations.*
import java.util.concurrent.CompletionStage

@Dao
interface OrderCassandraDao {
    @Insert
    fun create(orderDto: OrderCassandraDto): CompletionStage<Unit>

    @Select
    fun read(id: String): CompletionStage<OrderCassandraDto?>

    @Delete(customWhereClause = "id = :id", entityClass = [OrderCassandraDto::class])
    fun delete(id: String): CompletionStage<Boolean>

    @QueryProvider(providerClass = OrderCassandraSearchProvider::class, entityHelpers = [OrderCassandraDto::class])
    fun search(filter: DbOrderFilterRequest): CompletionStage<Collection<OrderCassandraDto>>
}