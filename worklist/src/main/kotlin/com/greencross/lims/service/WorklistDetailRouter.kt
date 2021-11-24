package com.greencross.lims.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
open class WorklistDetailRouter(private val handler: WorklistDetailHandler) {
    @Bean
    open fun WorklistDetailRouter(): RouterFunction<ServerResponse?> {
        return RouterFunctions
            .route(RequestPredicates.GET("/worklist/sample/{yesterday}&{today}"), this::findSample)
            .andRoute(RequestPredicates.GET("/worklist/work/{id}"), this::findWork)
    }

    private fun findSample(request: ServerRequest): Mono<ServerResponse> {
        return handler.sample(request.pathVariable("yesterday"), request.pathVariable("today")).collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.noContent().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
    private fun findWork(request: ServerRequest): Mono<ServerResponse>{
        return handler.list(request.pathVariable("id")).collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.noContent().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }

}