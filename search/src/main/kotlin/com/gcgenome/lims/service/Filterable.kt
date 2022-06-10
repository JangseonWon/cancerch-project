package com.gcgenome.lims.service

import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.sql.RelationalPathBase

interface Filterable<E> {
    fun table(): RelationalPathBase<E>
    fun pk(): ComparablePath<*>
    fun column(key: String): ComparableExpression<*>
    fun predicate(key: String?, value: String?): Predicate?
}