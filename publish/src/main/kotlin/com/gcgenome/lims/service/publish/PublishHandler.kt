package com.gcgenome.lims.service.publish

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.alis.Client
import com.gcgenome.alis.models.AlisRequest
import com.gcgenome.alis.models.AlisResponse
import com.gcgenome.lims.data.MessagePublish
import com.gcgenome.lims.data.MessageReport
import com.gcgenome.lims.service.report.ReportDao
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.time.Instant
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

@Service
class PublishHandler(
    private val reportDao: ReportDao,
    private val client: Client,
    private val om: ObjectMapper
    ) {
    private val publisher = Sinks.many().unicast().onBackpressureBuffer<AlisResponse>()
    private val subscriber = Sinks.many().multicast().directAllOrNothing<AlisResponse>()

    fun subscribe(): Flux<AlisResponse> = subscriber.asFlux()
    fun publish(sample: Long, service: String, createAt: Long, request: ServerRequest) : Mono<Boolean> {
        return reportDao.findForCassandraReport(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
            .zipWith(getUser())
            .flatMap {
                val file = it.t1.file
                val user = it.t2.authentication
                client.send(sample, service, file, user, request)
            }
            .filter{
                it == true
            }
            .flatMap {
                reportDao.merge(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
            }.map { true }
            .switchIfEmpty(Mono.just(false))
    }
    private fun getUser() : Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }

    @Bean("broadcast-publishing")
    fun broadcastPublish(): Consumer<String> {
        return Consumer { c: String -> subscriber.tryEmitNext(stringToMessage(c))}
    }
    private fun stringToMessage(str: String): AlisResponse {
        return om.readValue(str, AlisResponse::class.java)
    }
}