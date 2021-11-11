package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.data.MessageWorklist
import com.greencross.lims.data.Worklist

import com.greencross.lims.entity.QWorklist.worklist
import com.greencross.lims.repo.WorklistRepository
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.util.retry.Retry
import java.time.Duration
import java.util.*
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

    fun list(chkr: String): Flux<Worklist> {
        if(chkr.toBoolean()) return repo.findByStateAndActivation("open", "TRUE").map(mapper::toDto).sort(Comparator.comparing<Worklist?, Double?> { r->r.no()}.reversed())
        else return repo.findByActivation("TRUE").map(mapper::toDto).sort(Comparator.comparing<Worklist?, Double?> { r->r.no()}.reversed())
    }
    fun getNo(): Mono<Long>{
        return repo.findAll().map(mapper::toDto).count()
    }
    fun save(item: Worklist): Mono<Void> {
        return toEntity(item).flatMap(repo::save).then(Mono.empty())
    }
    private fun toEntity(item: Worklist) : Mono<com.greencross.lims.entity.Worklist> {
        val entity = com.greencross.lims.entity.Worklist(
            id = UUID.randomUUID(),
            no = item.no().toInt(),
            title = item.title(),
            sample = item.sample().toLong(),
            state = item.state(),
            comment = item.comment(),
            activation = "TRUE"
        )
        return Mono.just(entity)
    }
    fun update(item: Worklist): Mono<Void> {
        return repo.findById(UUID.fromString(item.id()))
            .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
            .map{s->updating(s, item)}
            .flatMap(repo::save)
            .then(Mono.empty())
    }
    private fun updating(item: com.greencross.lims.entity.Worklist, info: Worklist) : com.greencross.lims.entity.Worklist{
        item.title = info.title()
        item.comment = info.comment()
        item.sample = info.sample().toLong()
        item.state = info.state()
        return item
    }
    fun close(id: String): Mono<Void>{
        return repo.findById(UUID.fromString(id))
            .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
            .map{s->closing(s)}
            .flatMap(repo::save)
            .then(Mono.empty())
    }
    private fun closing(item: com.greencross.lims.entity.Worklist) : com.greencross.lims.entity.Worklist{
        item.state = "close"
        return item
    }
    fun delete(id: String): Mono<Void>{
        return repo.findById(UUID.fromString(id))
            .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
            .map{s->deleting(s)}
            .flatMap(repo::save)
            .then(Mono.empty())
    }
    private fun deleting(item: com.greencross.lims.entity.Worklist) : com.greencross.lims.entity.Worklist{
        item.activation = "FALSE"
        return item
    }
    private fun map(dto: MessageWorklist): String {
        return om.writeValueAsString(dto)
    }
    private fun map(json: String): MessageWorklist {
        return om.readValue(json, MessageWorklist::class.java)
    }
    fun subscribe(): Flux<MessageWorklist>{
        return subscriber.asFlux()
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