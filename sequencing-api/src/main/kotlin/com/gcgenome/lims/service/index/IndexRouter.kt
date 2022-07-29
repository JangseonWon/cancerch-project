package com.gcgenome.lims.service.index

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class IndexRouter(private val handler: IndexHandler) {
    @Bean("WorklistRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        GET("/plates/{plate}/indices",          contentType(MediaType("application", "vnd.avoid.v1", Charsets.UTF_8)), ::indices)
    }
    private fun indices(request: ServerRequest): Mono<ServerResponse>{
        return handler.indices(request.pathVariable("plate")).collectList()
            .flatMap { ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(it) }
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
           // .onErrorResume(Exception::class.java) { ServerResponse.badRequest().bodyValue(it.localizedMessage) }
    }
}