package com.greencross.lims.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
open class ReportRouter(private val handler: ReportHandler) {
    @Bean
    open fun ReportRouter(): RouterFunction<ServerResponse?>{
        return RouterFunctions
            .route(RequestPredicates.GET("/worklist/report"), this::report)
    }

    private fun report(request: ServerRequest): Mono<ServerResponse> {
        return handler.report().collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.noContent().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
}