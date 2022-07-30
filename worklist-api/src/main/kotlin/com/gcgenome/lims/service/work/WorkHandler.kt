package com.gcgenome.lims.service.work

import com.gcgenome.lims.projection.Work
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import java.util.*

@Service
class WorkHandler(val dao: WorkDao) {
    @Transactional
    fun works(worklist: UUID): Flux<Work> {
        return dao.findByWorklist(worklist)
    }
}