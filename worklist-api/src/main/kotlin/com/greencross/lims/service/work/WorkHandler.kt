package com.greencross.lims.service.work

import com.greencross.lims.projection.Work
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.*

@Service
class WorkHandler(val dao: WorkDao) {
    @Transactional
    fun works(worklist: UUID): Mono<Work> {
        return dao.findByWorklist(worklist.toString())
    }
}