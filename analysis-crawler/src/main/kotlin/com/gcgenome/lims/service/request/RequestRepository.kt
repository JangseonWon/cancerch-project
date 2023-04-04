package com.gcgenome.lims.service.request

import com.gcgenome.lims.entity.Request
import com.gcgenome.querydsl.PersistQuerydslR2dbcRepo
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface RequestRepository: PersistQuerydslR2dbcRepo<Request, Request.Companion.RequestPK> {
    fun findBySample(sample: Long): Flux<Request>
}