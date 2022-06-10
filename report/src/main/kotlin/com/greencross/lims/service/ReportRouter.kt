package com.greencross.lims.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class ReportRouter(
    private val handler: ReportHandler
) {
//    @Bean("ReportRouter")
//    fun route() = router{
//         GET("/samples/{sample}/services/{service}/reports", contentType(MediaType("application", "vnd.report.v1+json", Charsets.UTF_8)), ::reports)
//    }
//    private fun reports(request: ServerRequest): Mono<ServerResponse> {
//        val sample = request.pathVariable("sample").toLong()
//        val service = request.pathVariable("service")
//        return handler.reports(sample, service).collectList()
//            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
//            .switchIfEmpty(ServerResponse.noContent().build())
//    }
//    private fun preview(request: ServerRequest): Mono<ServerResponse>{
//        val sample = request.pathVariable("sample").toLong()
//        val service = request.pathVariable("service")
//        return request.bodyToMono()
//    }
//    private fun print(request: ServerRequest): Mono<ServerResponse>{
//        val sample = request.pathVariable("sample").toLong()
//        val service = request.pathVariable("service")
//        return handler.print(sample, service).
//    }
}