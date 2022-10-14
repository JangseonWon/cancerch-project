package com.greencross.lims.service.report

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.router
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Configuration
class ReportRouter(
    private val handler: ReportHandler
) {
    @Bean("com.greencross.lims.service.Report.ReportRouter")
    fun router() = router{
        PUT("/samples/{sample}/services/{service}/print/{lang}", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::print)
        GET("/samples/{sample}/services/{service}/reports/{createAt}", contentType(MediaType("application", "vnd.avoid.v1", Charsets.UTF_8)), ::preview)

    }
    private fun print(request: ServerRequest): Mono<ServerResponse>{
        val sample = request.pathVariable("sample")
        val service = request.pathVariable("service")
        val lang = request.pathVariable("lang")
        return handler.print(sample.toLong(), service, lang)
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
    private fun preview(request: ServerRequest): Mono<ServerResponse>{
        val sample = request.pathVariable("sample").toString().replace("-", "")
        val service = request.pathVariable("service")
        val createAt = request.pathVariable("createAt")
        return handler.preview(sample.toLong(), service, createAt.toLong())
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)::bodyValue)
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
}