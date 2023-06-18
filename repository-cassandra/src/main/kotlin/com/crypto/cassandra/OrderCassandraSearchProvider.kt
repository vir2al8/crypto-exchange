package com.crypto.cassandra

import com.crypto.cassandra.model.OrderCassandraDto
import com.crypto.cassandra.model.toTransport
import com.crypto.common.models.CommonOrderOperation
import com.crypto.common.models.CommonOrderStatus
import com.crypto.common.models.CommonOrderType
import com.crypto.common.repository.DbOrderFilterRequest
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class OrderCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<OrderCassandraDto>
) {
    fun search(filter: DbOrderFilterRequest): CompletionStage<Collection<OrderCassandraDto>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.operation != CommonOrderOperation.NONE) {
            select = select
                .whereColumn(OrderCassandraDto.COLUMN_OPERATION)
                .isEqualTo(QueryBuilder.literal(filter.operation.toTransport(), context.session.context.codecRegistry))
        }
        if (filter.type != CommonOrderType.NONE) {
            select = select
                .whereColumn(OrderCassandraDto.COLUMN_TYPE)
                .isEqualTo(QueryBuilder.literal(filter.type.toTransport(), context.session.context.codecRegistry))
        }
        if (filter.status != CommonOrderStatus.NONE) {
            select = select
                .whereColumn(OrderCassandraDto.COLUMN_STATUS)
                .isEqualTo(QueryBuilder.literal(filter.status.toTransport(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<OrderCassandraDto>()
        private val future = CompletableFuture<Collection<OrderCassandraDto>>()
        val stage: CompletionStage<Collection<OrderCassandraDto>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}
