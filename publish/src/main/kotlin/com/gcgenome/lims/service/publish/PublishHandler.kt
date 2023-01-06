package com.gcgenome.lims.service.publish

import com.gcgenome.alis.Client
import com.gcgenome.alis.models.AlisRequest
import com.gcgenome.alis.models.AlisResponse
import com.gcgenome.lims.service.report.ReportDao
import org.slf4j.LoggerFactory
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
import java.util.function.Supplier

@Service
class PublishHandler(
    val reportDao: ReportDao,
    val client: Client,
    ) {
    private val publisher = Sinks.many().unicast().onBackpressureBuffer<AlisRequest>()
    private val subscriber = Sinks.many().multicast().directAllOrNothing<AlisResponse>()
    private val logger = LoggerFactory.getLogger(PublishHandler::class.java)
//    @Transactional
//    fun publish2(sample: Long, service: String, createAt: Long) : Mono<Void> {
//        return reportDao.findForCassandraReport(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
//            .flatMap{
//                publisher.tryEmitNext("test")
//                Mono.empty()
//            }
//    }
    fun publish(sample: Long, service: String, createAt: Long, request: ServerRequest) : Mono<Boolean> {
        return reportDao.findForCassandraReport(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
            .zipWith(getUser())
            .flatMap {
                val file = it.t1.file
                val user = it.t2.authentication
                client.send(sample, service, file, user, request)
            }
            .filter{
                it.outcome == "SUCCESS"
            }
            .flatMap {
                reportDao.merge(sample, service, LocalDateTime.ofInstant(Instant.ofEpochMilli(createAt), TimeZone.getDefault().toZoneId()))
            }.map { true }
            .switchIfEmpty(Mono.just(false))
    }
    private fun getUser() : Mono<SecurityContext> {
        return ReactiveSecurityContextHolder.getContext()
    }

    fun publishReport(): Supplier<Flux<AlisRequest>> {
        return Supplier { publisher.asFlux() }
    }
}