package com.greencross.lims.service

import com.greencross.lims.data.Worklist
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
open class WorklistRouter(private val handler: WorklistHandler) {
    @Bean
    open fun WorklistRouterInstance(): RouterFunction<ServerResponse?> {
        return RouterFunctions
            .route(RequestPredicates.GET("/worklist"), this::findAll)
            .andRoute(RequestPredicates.GET("/worklist/changes"), this::subscribeWorklist)
    }
    private fun findAll(request: ServerRequest): Mono<ServerResponse> {
        return handler.list().collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.noContent().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
    private fun subscribeWorklist(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
            .body(BodyInserters.fromServerSentEvents(handler.subscribe().map{
                msg -> ServerSentEvent.builder<Worklist>(msg.data).event(msg.type.name).id(msg.data.id()).build()
            }))
    }
}