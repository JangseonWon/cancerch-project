package com.gcgenome.lims.service.index

import com.gcgenome.lims.data.Index
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux

@Service
@Transactional(readOnly = true)
class IndexHandler(private val dao: IndexDao) {
    fun indexes(plate: String): Flux<Index> = dao.findByPlate(plate)
}