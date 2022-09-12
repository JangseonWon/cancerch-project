package com.gcgenome.lims.service.work

import com.gcgenome.lims.SecurityContextRepository
import com.gcgenome.lims.entity.QPreprocessing.preprocessing
import com.gcgenome.lims.entity.QUser
import com.gcgenome.lims.entity.QWork.work
import com.gcgenome.lims.projection.Work
import com.querydsl.core.types.Path
import com.querydsl.core.types.Projections.constructor
import com.querydsl.sql.SQLQuery
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.*

@Component
class WorkDao(private val repo: WorkRepository) {
    private val createBy = QUser("Creator")
    private val modifyBy = QUser("Modifier")
    private fun select(query: SQLQuery<*>): SQLQuery<Work.Companion.WorkBuilder>{
        @Suppress("UNCHECKED_CAST")
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
                work.x,
                work.y,
                preprocessing.json.`as`("json"),
                work.createAt.`as`("createAt"),
                createBy.id.`as`("createById"),
                createBy.name.`as`("createBy"),
                preprocessing.lastModifyAt.`as`("lastModifyAt"),
                preprocessing.lastModifyBy.`as`("lastModifyById"),
                modifyBy.name.`as`("lastModifyBy"),
                preprocessing.concNa.`as`("concNa"),
                preprocessing.concInput.`as`("concInput"),
                preprocessing.libPrep.`as`("libPrep"),
                preprocessing.libConcTape.`as`("libConcTape"),
                preprocessing.libConcQubit.`as`("libConcQubit"),
                preprocessing.fragmentSize.`as`("fragmentSize"),
                preprocessing.amount.`as`("amount"),
                preprocessing.dilution,
                preprocessing.volume,
                preprocessing.libVolume.`as`("libVolume"),
                preprocessing.bufferVolume.`as`("bufferVolume"),
                preprocessing.qc,
                preprocessing.indexI7.`as`("indexI7"),
                preprocessing.sequenceI7.`as`("sequenceI7"),
                preprocessing.indexI5.`as`("indexI5"),
                preprocessing.sequenceI5.`as`("sequenceI5")
            )
        ).from(work)
            .leftJoin(preprocessing).on(preprocessing.worklist.eq(work.worklist as Path<UUID>).and(preprocessing.index.eq(work.index)))
            .leftJoin(createBy).on(createBy.id.eq(work.createBy))
            .leftJoin(modifyBy).on(modifyBy.id.eq(preprocessing.lastModifyBy))
    }
    fun findByWorklist(worklist: String): Flux<Work> {
        return ReactiveSecurityContextHolder.getContext().flatMapMany { context->
            repo.query{
                select(it).where(work.worklist.eq(worklist)).orderBy(work.index.asc())
            }.all().map {
                it.apply {
                    if(!context.authentication.authorities.contains(SecurityContextRepository.Companion.RoleManager)) {
                        patientName = "*"
                        mrns = "*"
                    }
                }
            }
        }.map(Work.Companion.WorkBuilder::build)
    }
}