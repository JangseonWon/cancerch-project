package com.gcgenome.lims.service.reportfile

import org.springframework.data.cassandra.core.CassandraOperations
import org.springframework.data.cassandra.core.InsertOptions
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation
import org.springframework.data.cassandra.repository.support.SimpleCassandraRepository

class CassandraRepositoryExtendedImpl<T, ID>(
    metadata: CassandraEntityInformation<T, ID>,
    operations: CassandraOperations
) :
    SimpleCassandraRepository<T, ID>(metadata, operations), CassandraRepositoryExtended<T, ID> {
    private val entityInformation: CassandraEntityInformation<T, ID>
    private val operations: CassandraOperations

    init {
        this.entityInformation = metadata
        this.operations = operations
    }

    override fun <S : T?> save(entity: S, ttl: Int): S {
        val insertOptions = InsertOptions.builder().ttl(ttl).build()
        operations.insert(entity, insertOptions)
        return entity
    }
}