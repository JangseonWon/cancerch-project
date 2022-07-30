package com.gcgenome.lims.service.work

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class WorkRouter(private val handler: WorkHandler) {
    @Bean("com.greencross.lims.service.WorkRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        GET("/worklist/{id}/works", contentType(MediaType("application", "vnd.avoid.v1", Charsets.UTF_8)), ::works)
    }
    private fun works(request: ServerRequest): Mono<ServerResponse> {
        return handler.works(request.pathVariable("id")).collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
    }
}