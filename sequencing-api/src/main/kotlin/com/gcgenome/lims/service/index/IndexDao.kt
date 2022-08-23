package com.gcgenome.lims.service.index

import com.gcgenome.lims.data.Index
import com.gcgenome.lims.entity.QIndex.index
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class IndexDao(private val repo: IndexRepository) {
    private fun select(query: SQLQuery<*>): SQLQuery<Index>{
        return query.select(
            constructor(
                Index::class.java,
                index.id,
                index.type,
                index.sequence,
                index.plate,
                index.position
            )
        ).from(index)
    }
    fun findByPlate(plate: String): Flux<Index> {
        return repo.query{
            select(it).where(index.plate.eq(plate)).orderBy(index.type.desc(), index.position.asc())
        }.all()
    }
}