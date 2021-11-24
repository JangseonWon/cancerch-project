package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.data.MessageWorklist
import com.greencross.lims.data.PageReactive
import com.greencross.lims.data.Worklist

import com.greencross.lims.entity.QWorklist.worklist
import com.greencross.lims.entity.User
import com.greencross.lims.repo.SecurityContextRepository
import com.greencross.lims.repo.WorklistRepository
import com.greencross.lims.data.Query_
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
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

    fun list(query: Query_, chkr: String): Mono<PageReactive<Worklist>>{
        val q : Mono<Query_> = if(chkr.toBoolean()){
            query.filters.add(Query_.Companion.Filter("state", "open"))
            query.filters.add(Query_.Companion.Filter("activation", "TRUE"))
            Mono.just(query)

        }else {
            query.filters.add(Query_.Companion.Filter("activation", "TRUE"))
            Mono.just(query)
        }
        return q.flatMap(this::total).map { total->PageReactive(total, query.limit, query.page, data(query))}
    }
    fun data(query: Query_) :Flux<Worklist> {
        return repo.query { q->
            q.select(repo.entityProjection())
                .from(worklist)
                .offset(query.page*query.limit.toLong())
                .limit(query.limit.toLong())
                .orderBy(sort(query.sortBy, query.asc))
                .where(predicate(query))
        }.all().map(mapper::toDto).sort(Comparator.comparing<Worklist?, String?> { r->r.no.toString()}.reversed())
    }
    private fun sort(key: String?, asc: Boolean?): OrderSpecifier<*> {
        val order = if(false == asc) Order.DESC else Order.ASC
        return OrderSpecifier(order, worklist.no)
    }
    private fun total(query: Query_): Mono<Long> {
        return repo.query { q-> q.select(worklist.id.count()).from(worklist).where(predicate(query))}.first()
    }
    private fun predicate(query: Query_): Predicate {
        val builder = BooleanBuilder()
        query.filters.forEach { filter->
            run {
                val predicate = predicate(filter.key, filter.value)
                if(predicate!=null) builder.and(predicate)
            }
        }
        return builder
    }
    private fun predicate(key: String?, value: String?): Predicate? {
        when {
            key == null || key.trim().isEmpty() -> {
                val predicates = listOfNotNull(
                    predicate("file", value),
                )
                return BooleanBuilder().andAnyOf(*predicates.toTypedArray())
            }
            "state".contentEquals(key, ignoreCase = true) -> return if(value != null) worklist.state.`in`(value.split(",")) else null
            "activation".contentEquals(key, ignoreCase = true) -> return if (value != null) worklist.activation.`in`(value.split(",")) else null
            else -> return null
        }
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