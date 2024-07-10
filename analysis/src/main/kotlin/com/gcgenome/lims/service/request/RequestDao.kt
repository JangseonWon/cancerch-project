package com.gcgenome.lims.service.request

import com.gcgenome.lims.entity.QRequest.request
import com.querydsl.core.types.dsl.Wildcard
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RequestDao(private val repo: RequestRepository) {
    fun checkExistRequest(sample: Long, service: String): Mono<Boolean> {
        return repo.query {
            it.select(Wildcard.all).from(request)
                .where(request.sample.eq(sample)
                    .and(request.service.eq(service)))
        }.all().count().map { it != 0L }
    }
}
