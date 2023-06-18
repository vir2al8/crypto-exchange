package com.crypto.cassandra

import com.crypto.common.models.CommonOrder
import com.crypto.common.repository.OrderRepository
import com.crypto.repotest.RepositoryOrderCreateTest
import com.crypto.repotest.RepositoryOrderDeleteTest
import com.crypto.repotest.RepositoryOrderReadTest
import com.crypto.repotest.RepositoryOrderSearchTest
import org.testcontainers.containers.CassandraContainer
import java.time.Duration
import java.util.UUID

class RepositoryOrderCassandraCreateTest : RepositoryOrderCreateTest() {
    override val repository: OrderRepository = TestCompanion.repository(initObjects, "ks_create")
}

class RepositoryOrderCassandraDeleteTest : RepositoryOrderDeleteTest() {
    override val repository: OrderRepository = TestCompanion.repository(initObjects, "ks_delete")
}

class RepositoryOrderCassandraReadTest : RepositoryOrderReadTest() {
    override val repository: OrderRepository = TestCompanion.repository(initObjects, "ks_read")
}

class RepositoryOrderCassandraSearchTest : RepositoryOrderSearchTest() {
    override val repository: OrderRepository = TestCompanion.repository(initObjects, "ks_search")
}

class TestCasandraContainer : CassandraContainer<TestCasandraContainer>("cassandra:4.1.2")

object TestCompanion {
    private val container by lazy {
        TestCasandraContainer().withStartupTimeout(Duration.ofSeconds(300L))
            .also { it.start() }
    }

    fun repository(initObjects: List<CommonOrder>, keyspace: String): OrderRepositoryCassandra {
        return OrderRepositoryCassandra(
            keyspaceName = keyspace,
            host = container.host,
            port = container.getMappedPort(CassandraContainer.CQL_PORT),
            testing = true,
            randomUuid = { UUID.randomUUID().toString() },
            initObjects = initObjects
        )
    }
}
