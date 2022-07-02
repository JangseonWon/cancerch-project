package com.gcgenome.lims.service.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.service.searchParam
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class ReportRouter(
    private val handler: ReportHandler
) {
    @Bean("com.greencross.lims.service.ReportRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        GET("/analysis/sample/{sample}/service/{service}", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::search)
    }
    private fun search(request: ServerRequest): Mono<ServerResponse> {
        return handler.reports(request.pathVariable("sample").toLong(), request.pathVariable("service"))
            .collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
}