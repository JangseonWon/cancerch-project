package com.greencross.lims.service.reportfile

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface CassandraRepositoryExtended<T, ID> : CassandraRepository<T, ID> {
    fun <S : T?> save(entity: S, ttl: Int): S
}