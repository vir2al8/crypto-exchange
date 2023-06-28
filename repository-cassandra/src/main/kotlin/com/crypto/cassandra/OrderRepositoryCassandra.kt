package com.crypto.cassandra

import com.crypto.cassandra.model.OrderCassandraDto
import com.crypto.cassandra.model.OrderOperationCassandra
import com.crypto.cassandra.model.OrderStatusCassandra
import com.crypto.cassandra.model.OrderTypeCassandra
import com.crypto.common.helpers.asCommonError
import com.crypto.common.models.CommonError
import com.crypto.common.models.CommonOrder
import com.crypto.common.models.CommonOrderId
import com.crypto.common.models.CommonOrderStatus
import com.crypto.common.repository.*
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.net.InetSocketAddress
import java.time.Instant
import java.util.UUID
import java.util.concurrent.CompletionStage
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class OrderRepositoryCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val username: String = "cassandra",
    private val password: String = "cassandra",
    private val testing: Boolean = false,
    private val timeout: Duration = 30.toDuration(DurationUnit.SECONDS),
    private val randomUuid: () -> String = { UUID.randomUUID().toString() },
    initObjects: Collection<CommonOrder> = emptyList(),
) : OrderRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(OrderOperationCassandra::class.java))
            register(EnumNameCodec(OrderStatusCassandra::class.java))
            register(EnumNameCodec(OrderTypeCassandra::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter("datacenter1")
            .withAuthCredentials(username, password)
            .withCodecRegistry(codecRegistry)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private fun createSchema(keyspace: String) {
        session.execute(
            SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(1)
                .build()
        )
        session.execute(OrderCassandraDto.table(keyspace, OrderCassandraDto.TABLE_NAME))
    }

    private val dao by lazy {
        if (testing) {
            createSchema(keyspaceName)
        }
        mapper.orderDao(keyspaceName, OrderCassandraDto.TABLE_NAME).apply {
            runBlocking {
                initObjects.map { model ->
                    withTimeout(timeout) {
                        create(OrderCassandraDto(model)).await()
                    }
                }
            }
        }
    }

    private fun errorToOrderResponse(e: Exception) = DbOrderResponse.error(e.asCommonError())
    private fun errorToOrdersResponse(e: Exception) = DbOrdersResponse.error(e.asCommonError())

    private suspend inline fun <DbRes, Response> doDbAction(
        name: String,
        crossinline daoAction: () -> CompletionStage<DbRes>,
        okToResponse: (DbRes) -> Response,
        errorToResponse: (Exception) -> Response
    ): Response = doDbAction(
        name,
        {
            val dbRes = withTimeout(timeout) { daoAction().await() }
            okToResponse(dbRes)
        },
        errorToResponse
    )

    private suspend inline fun readAndDoDbAction(
        name: String,
        id: CommonOrderId,
        successResult: CommonOrder?,
        daoAction: () -> CompletionStage<Boolean>,
        errorToResponse: (Exception) -> DbOrderResponse
    ): DbOrderResponse =
        if (id == CommonOrderId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            name,
            {
                val read = dao.read(id.asString()).await()
                if (read == null) ID_NOT_FOUND
                else {
                    val success = daoAction().await()
                    if (success) DbOrderResponse.success(successResult ?: read.toOrderModel())
                    else DbOrderResponse(
                        read.toOrderModel(),
                        false,
//                        CONCURRENT_MODIFICATION.errors
                    )
                }
            },
            errorToResponse
        )

    private inline fun <Response> doDbAction(
        name: String,
        daoAction: () -> Response,
        errorToResponse: (Exception) -> Response
    ): Response =
        try {
            daoAction()
        } catch (e: Exception) {
            log.error("Failed to $name", e)
            errorToResponse(e)
        }

    override suspend fun createOrder(rq: DbOrderRequest): DbOrderResponse {
        val createdAt = Instant.now()
        val new = rq.order.copy(id = CommonOrderId(randomUuid()), createdAt = createdAt, updatedAt = createdAt, status = CommonOrderStatus.OPEN)
        return doDbAction(
            "create",
            { dao.create(OrderCassandraDto(new)) },
            { DbOrderResponse.success(new) },
            ::errorToOrderResponse
        )
    }

    override suspend fun readOrder(rq: DbOrderIdRequest): DbOrderResponse =
        if (rq.id == CommonOrderId.NONE)
            ID_IS_EMPTY
        else doDbAction(
            "read",
            { dao.read(rq.id.asString()) },
            { found ->
                if (found != null) DbOrderResponse.success(found.toOrderModel())
                else ID_NOT_FOUND
            },
            ::errorToOrderResponse
        )

    override suspend fun deleteOrder(rq: DbOrderIdRequest): DbOrderResponse =
        readAndDoDbAction(
            "delete",
            rq.id,
            null,
            { dao.delete(rq.id.asString()) },
            ::errorToOrderResponse
        )

    override suspend fun searchOrder(rq: DbOrderFilterRequest): DbOrdersResponse =
        doDbAction(
            "search",
            { dao.search(rq) },
            { found ->
                DbOrdersResponse.success(found.map { it.toOrderModel() })
            },
            ::errorToOrdersResponse
        )

    companion object {
        private val ID_IS_EMPTY = DbOrderResponse.error(CommonError(field = "id", message = "Id is empty"))
        private val ID_NOT_FOUND =
            DbOrderResponse.error(CommonError(field = "id", code = "not-found", message = "Not Found"))
    }
}

private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
    .split(Regex("""\s*,\s*"""))
    .map { InetSocketAddress(InetAddress.getByName(it), port) }