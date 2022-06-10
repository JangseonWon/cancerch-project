package com.gcgenome.lims.service

import com.gcgenome.lims.search.PageReactive
import com.gcgenome.lims.search.SearchParam
import com.infobip.spring.data.r2dbc.QuerydslR2dbcFragment
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import com.querydsl.sql.SQLQuery
import reactor.core.publisher.Mono

abstract class Searchable<E> (
    private val repo: QuerydslR2dbcFragment<E>
): Filterable<E> {
    private fun predicate(param: SearchParam): Predicate {
        val builder = BooleanBuilder()
        if(param.filters != null) param.filters.forEach {
            val predicate = predicate(it.key, it.value)
            if(predicate!=null) builder.and(predicate)
        }
        return builder
    }
    open fun from(query: SQLQuery<E>, param: SearchParam): SQLQuery<E> = query.from(table())
    fun search(param: SearchParam) : Mono<PageReactive<E>> {
        val predicates = predicate(param)
        val flux = repo.query {
            if(param.sortBy!=null) {
                val expression = column(param.sortBy)
                it.orderBy(if(param.asc!=null && param.asc) expression.asc() else expression.desc())
            }
            if(param.limit!=null && param.page!=null) it.limit(param.limit.toLong()).offset(param.page*param.limit.toLong())
            from(it.select(repo.entityProjection()), param).where(predicates)
        }.all()
        val count = repo.query { it.select(pk().count()).from(table()).where(predicates) }
        return count.one().map { PageReactive(it, param.limit, param.page, flux) }
    }
}