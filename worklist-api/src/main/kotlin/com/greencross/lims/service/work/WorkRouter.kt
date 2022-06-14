package com.greencross.lims.service.work

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Configuration
class WorkRouter(private val handler: WorkHandler) {
    @Bean("com.greencross.lims.service.WorkRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        GET("/worklist/{id}/works", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::works)
    }
    private fun works(request: ServerRequest): Mono<ServerResponse> {
        return handler.works(UUID.fromString(request.pathVariable("id")))
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
//            .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build())
    }
}