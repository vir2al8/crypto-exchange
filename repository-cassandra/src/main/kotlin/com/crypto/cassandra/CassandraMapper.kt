package com.crypto.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface CassandraMapper {
    @DaoFactory
    fun orderDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): OrderCassandraDao

    companion object {
        fun builder(session: CqlSession) = CassandraMapperBuilder(session)
    }
}