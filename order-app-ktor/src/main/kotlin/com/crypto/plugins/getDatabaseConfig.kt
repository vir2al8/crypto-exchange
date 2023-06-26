package com.crypto.plugins

import com.crypto.cassandra.OrderRepositoryCassandra
import com.crypto.common.repository.OrderRepository
import com.crypto.configs.CassandraConfig
import com.crypto.configs.ConfigPaths
import com.crypto.configs.OrderDbType
import io.ktor.server.application.*

fun Application.getDatabaseConf(type: OrderDbType): OrderRepository {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "cassandra" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'cassandra'"
        )
    }
}

private fun Application.initCassandra(): OrderRepository {
    val config = CassandraConfig(environment.config)
    return OrderRepositoryCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        username = config.username,
        password = config.password,
    )
}