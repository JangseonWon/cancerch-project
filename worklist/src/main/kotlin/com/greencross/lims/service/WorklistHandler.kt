package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.data.MessageWorklist
import com.greencross.lims.entity.Worklist
import com.greencross.lims.entity.QWorklist.worklist
import com.greencross.lims.repo.WorklistRepository
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.util.function.Consumer
import java.util.function.Supplier

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

    fun subscribe(): Flux<MessageWorklist>{
        return subscriber.asFlux()
    }

    private fun map(dto: MessageWorklist): String {
        return om.writeValueAsString(dto)
    }

    private fun map(json: String): MessageWorklist {
        return om.readValue(json, MessageWorklist::class.java)
    }

    @Bean("publish-worklist")
    fun publishModel(): Supplier<Flux<String>> {
        return Supplier { publisher.asFlux().map(this::map) }
    }
    @Bean("broadcast-worklist")
    fun broadcastModel(): Consumer<String> {
        return Consumer { json -> subscriber.tryEmitNext(map(json)) }
    }
}