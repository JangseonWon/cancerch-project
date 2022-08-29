package com.gcgenome.lims.service.analysis

import com.gcgenome.lims.entity.Analysis
import com.gcgenome.lims.entity.QAnalysis.analysis
import com.gcgenome.lims.search.PageReactive
import com.gcgenome.lims.search.SearchParam
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.ComparableExpression
import com.querydsl.core.types.dsl.Wildcard
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class AnalysisDao(private val repo: AnalysisRepository) {
    fun column(key: String): ComparableExpression<*> {
        return when {
            key.trim().isEmpty()                -> analysis.sample.stringValue()
            "의뢰일".contentEquals(key)         -> analysis.dateRequest
            "ID".contentEquals(key)             -> analysis.sample.stringValue()
            "batch".contentEquals(key)          -> analysis.batch
            else                                -> analysis.sample.stringValue()
        }
    }
    fun predicate(key: String?, value: String?): Predicate? {
        return when {
            key == null || key.trim().isEmpty() -> {
                val predicates = listOfNotNull(
                    predicate("name", value),
                    predicate("remark", value),
                    predicate("top5", value),
                    predicate("top6", value),
                    predicate("ID", value)
                )
                BooleanBuilder().andAnyOf(*predicates.toTypedArray())
            }
            "ID".contentEquals(key, ignoreCase = true) -> return if(value != null)          analysis.sample.eq(value.replace("-", "").toLong())
             else null
            "name".contentEquals(key, ignoreCase = true) -> return if(value != null)        analysis.patientName.eq(value)          else null
            "remark".contentEquals(key, ignoreCase = true) -> return if(value != null)      analysis.remark.eq(value)               else null
            "published".contentEquals(key, ignoreCase = true) -> return if(value != null)   analysis.publishAt.isNull               else null
            "printed".contentEquals(key, ignoreCase = true) -> return if(value != null)     analysis.reportedAt.isNull              else null
            "too5".contentEquals(key, ignoreCase = true) -> return if(value != null)        analysis.too5Pred.eq(convert(value))    else null
            "too6".contentEquals(key, ignoreCase = true) -> return if(value != null)        analysis.too6Pred.eq(convert(value))    else null
            "to".contentEquals(key, ignoreCase = true) -> return if (value != null) {
                val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(value.toLong()), ZoneId.systemDefault())
                return analysis.dateRequest.loe(date)
            } else null
            "from".contentEquals(key, ignoreCase = true) -> return if (value != null) {
                val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(value.toLong()), ZoneId.systemDefault())
                return analysis.dateRequest.goe(date)
            } else null
            else -> null
        }
    }
    fun predicate(param: SearchParam): Predicate {
        val builder = BooleanBuilder()
        if(param.filters != null) param.filters.forEach {
            val predicate = predicate(it.key, it.value)
            if(predicate!=null) builder.and(predicate)
        }
        return builder
    }
    fun from(query: SQLQuery<Analysis>, param: SearchParam): SQLQuery<Analysis> = query.from(analysis)
    fun search(param: SearchParam): Mono<PageReactive<Analysis>> {
        val predicates = predicate(param)
        val flux = repo.query {
            if(param.sortBy!=null) {
                val expression = column(param.sortBy)
                it.orderBy(if(param.asc!=null && param.asc) expression.asc() else expression.desc())
            }
            if(param.limit!=null && param.page!=null) it.limit(param.limit.toLong()).offset(param.page*param.limit.toLong())
            from(it.select(repo.entityProjection()), param).where(predicates)
        }.all()
        val count = repo.query { it.select(Wildcard.count).from(analysis).where(predicates)}
        return count.one().map { PageReactive(it, param.limit, param.page, flux) }
    }
    private fun convert(cancer: String) = when(cancer){
        "폐암"   -> "LuC"
        "대장암" -> "colon"
        "난소암" -> "OV"
        "간암"   -> "HCC"
        "식도암" -> "ESO"
        "췌장암" -> "PanC"
        else   -> cancer
    }
}