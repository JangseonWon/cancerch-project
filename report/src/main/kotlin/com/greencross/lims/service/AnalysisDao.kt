//package com.greencross.lims.service
//
//import com.gcgenome.lims.entity.QUser
//import com.greencross.lims.entity.QAnalysis.analysis
//import com.greencross.lims.entity.readonly.QSample.sample
//import com.greencross.lims.projection.Analysis
//import com.greencross.lims.repo.AnalysisRepository
//import com.querydsl.core.types.Projections.constructor
//import com.querydsl.sql.SQLQuery
//import io.r2dbc.postgresql.codec.Json
//import org.springframework.stereotype.Repository
//import reactor.core.publisher.Mono
//
//@Repository
//class AnalysisDao(private val repo: AnalysisRepository) {
//    private val createBy = QUser("Creator")
//    private val modifyBy = QUser("Modifier")
//    private fun select(query: SQLQuery<*>): SQLQuery<Analysis.Companion.AnalysisBuilder> {
//        return query.select(
//            constructor(
//                Analysis.Companion.AnalysisBuilder::class.java,
//                analysis.sample,
//                analysis.service,
//                analysis.batch,
//                analysis.row,
//                createBy.id.`as`("createId"),
//                createBy.name.`as`("createBy"),
//                analysis.createAt.`as`("createAt"),
//                analysis.lastModifyBy.`as`("lastModifyId"),
//                modifyBy.name.`as`("lastModifyBy"),
//                analysis.lastModifyAt.`as`("lastModifyAt"),
//                analysis.file.`as`("file"),
//                analysis.value.`as`("value")
//            )
//        ).from(analysis)
//            .leftJoin(createBy).on(createBy.id.eq(analysis.createBy))
//            .leftJoin(modifyBy).on(modifyBy.id.eq(analysis.lastModifyBy))
//            .leftJoin(sample).on(sample.id.eq(analysis.sample))
//    }
//    fun findById(sample: Long, service: String) : Mono<Analysis> {
//        return repo.query{
//            select(it).where(analysis.sample.eq(sample).and(analysis.service.eq(service)))
//        }.one().map(Analysis.Companion.AnalysisBuilder::build)
//    }
//    fun deleteById(sample: Long, service: String) : Mono<Boolean>{
//        return repo.deleteWhere(analysis.sample.eq(sample).and(analysis.service.eq(service)))
//            .flatMap {
//
//
//                print(it)
//                when(it){
//                    0 -> Mono.empty()
//                    1 -> Mono.just(true)
//                    else -> Mono.error(RuntimeException())
//                }
//            }
//    }
//    fun merge(sample: Long, service: String, batch: String, row: Long, json: String): Mono<Any>{
//        return repo.query { it.select(
//            constructor(com.greencross.lims.entity.Analysis::class.java,
//                analysis.sample,
//                analysis.service,
//                analysis.batch,
//                analysis.row,
//                analysis.createAt,
//                analysis.createBy,
//                analysis.lastModifyAt,
//                analysis.lastModifyBy,
//                analysis.file,
//                analysis.value
//            )
//        ).from(analysis).where(analysis.sample.eq(sample).and(analysis.service.eq(service)))}
//            .one().switchIfEmpty(Mono.just(com.greencross.lims.entity.Analysis(sample, service)))
//            .map { it.apply { it.value = Json.of(json) }}
//            .flatMap { repo.save(it) }
//    }
//}