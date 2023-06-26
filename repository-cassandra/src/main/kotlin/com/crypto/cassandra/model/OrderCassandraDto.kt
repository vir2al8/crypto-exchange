package com.crypto.cassandra.model

import com.crypto.common.models.*
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import java.math.BigDecimal
import java.time.Instant

@Entity
data class OrderCassandraDto(
    @CqlName(COLUMN_ID)
    @PartitionKey
    var id: String? = null,
    @CqlName(COLUMN_WALLET_ID)
    var walletId: String? = null,
    @CqlName(COLUMN_AMOUNT)
    var amount: BigDecimal? = null,
    @CqlName(COLUMN_TYPE)
    var type: OrderTypeCassandra? = null,
    @CqlName(COLUMN_OPERATION)
    var operation: OrderOperationCassandra? = null,
    @CqlName(COLUMN_STATUS)
    var status: OrderStatusCassandra? = null,
    @CqlName(COLUMN_CREATED_AT)
    var createdAt: Instant? = null,
    @CqlName(COLUMN_UPDATED_AT)
    var updatedAt: Instant? = null
) {
    constructor(orderModel: CommonOrder) : this(
        id = orderModel.id.takeIf { it != CommonOrderId.NONE }?.asString(),
        walletId = orderModel.walletId.takeIf { it != CommonWalletId.NONE }?.asString(),
        amount = orderModel.amount.takeIf { it != BigDecimal.ZERO },
        type = orderModel.type.toTransport(),
        operation = orderModel.operation.toTransport(),
        status = orderModel.status.toTransport(),
        createdAt = orderModel.createdAt.takeIf { it != Instant.MIN },
        updatedAt = orderModel.updatedAt.takeIf { it != Instant.MIN },
    )

    fun toOrderModel(): CommonOrder =
        CommonOrder(
            id = id?.let { CommonOrderId(it) } ?: CommonOrderId.NONE,
            walletId = walletId?.let { CommonWalletId(it) } ?: CommonWalletId.NONE,
            amount = amount ?: BigDecimal.ZERO,
            type = type.fromTransport(),
            operation = operation.fromTransport(),
            status = status.fromTransport(),
            createdAt = createdAt ?: Instant.MIN,
            updatedAt = updatedAt ?: Instant.MIN,
        )

    companion object {
        const val TABLE_NAME = "orders"

        const val COLUMN_ID = "id"
        const val COLUMN_WALLET_ID = "wallet_id"
        const val COLUMN_TYPE = "type"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_OPERATION = "operation"
        const val COLUMN_STATUS = "status"
        const val COLUMN_CREATED_AT = "created_at"
        const val COLUMN_UPDATED_AT = "updated_at"

        fun table(keyspace: String, tableName: String) =
            SchemaBuilder
                .createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey(COLUMN_ID, DataTypes.TEXT)
                .withColumn(COLUMN_WALLET_ID, DataTypes.TEXT)
                .withColumn(COLUMN_TYPE, DataTypes.TEXT)
                .withColumn(COLUMN_AMOUNT, DataTypes.DECIMAL)
                .withColumn(COLUMN_OPERATION, DataTypes.TEXT)
                .withColumn(COLUMN_STATUS, DataTypes.TEXT)
                .withColumn(COLUMN_CREATED_AT, DataTypes.TIMESTAMP)
                .withColumn(COLUMN_UPDATED_AT, DataTypes.TIMESTAMP)
                .build()
    }
}