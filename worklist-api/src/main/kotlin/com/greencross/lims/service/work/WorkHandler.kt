package com.greencross.lims.service.work

import com.greencross.lims.data.Worklist
import com.greencross.lims.projection.Work
import com.greencross.lims.service.worklist.WorklistDao
import kotlinx.coroutines.reactive.collect
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class WorkHandler(val dao: WorkDao) {
    @Transactional
    fun works(worklist: String): Flux<Work> {
        return dao.findByWorklist(worklist)
    }
}