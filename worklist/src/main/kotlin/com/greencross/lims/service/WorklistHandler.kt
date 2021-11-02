package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.data.MessageWorklist
import com.greencross.lims.entity.Worklist
import com.greencross.lims.entity.QWorklist.worklist
import com.greencross.lims.repo.WorklistRepository
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks

@Service
class WorklistHandler(
    private val om: ObjectMapper,
    private val repo: WorklistRepository,
    private val mapper: WorklistToDto
) {
    private val publisher = Sinks.many().unicast().onBackpressureBuffer<MessageWorklist>()
    private val subscriber = Sinks.many().multicast().directAllOrNothing<MessageWorklist>()

    fun list(): Flux<com.greencross.lims.data.Worklist> {
        return repo.findAll(OrderSpecifier(Order.DESC, worklist.no)).map(mapper::toDto)
    }
}