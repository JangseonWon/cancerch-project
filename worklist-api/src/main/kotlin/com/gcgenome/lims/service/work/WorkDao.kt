package com.gcgenome.lims.service.work

import com.gcgenome.lims.projection.Work
import com.greencross.lims.entity.QPreprocessing.preprocessing
import com.greencross.lims.entity.QUser
import com.greencross.lims.entity.QWork.work
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class WorkDao(
    private val repo: WorkRepository
    ) {
    private val createBy = QUser("Creator")
    private val modifyBy = QUser("Modifier")
    private fun select(query: SQLQuery<*>): SQLQuery<Work.Companion.WorkBuilder>{
        return query.select(
            constructor(
                Work.Companion.WorkBuilder::class.java,
                work.worklist,
                work.index,
                work.samples,
                work.services,
                work.patientName.`as`("patientName"),
                work.mrns,
                work.gid,
                preprocessing.json.`as`("json"),
                work.createAt.`as`("createAt"),
                createBy.id.`as`("createById"),
                createBy.name.`as`("createBy"),
                preprocessing.lastModifyAt.`as`("lastModifyAt"),
                preprocessing.lastModifyBy.`as`("lastModifyById"),
                modifyBy.name.`as`("lastModifyBy")
            )
        ).from(work)
            .leftJoin(preprocessing).on(preprocessing.worklist.eq(work.worklist).and(preprocessing.index.eq(work.index)))
            .leftJoin(createBy).on(createBy.id.eq(work.createBy))
            .leftJoin(modifyBy).on(modifyBy.id.eq(preprocessing.lastModifyBy))
    }
    fun findByWorklist(worklist: String): Flux<Work> {
        return repo.query{
            select(it).where(work.worklist.eq(worklist)).orderBy(work.index.asc())
        }.all().map(Work.Companion.WorkBuilder::build)
    }
}