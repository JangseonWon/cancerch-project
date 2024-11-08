package com.greencross.lims.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@Configuration
open class ServiceRouter(private var handler: ServiceHandler) {
    @Bean
    open fun router() = router {
        GET("/services", ::services)
    }
    private fun services(request: ServerRequest): Mono<ServerResponse> {
        return handler.list(request)
            .flatMap { e-> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(e) }
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
            .onErrorResume (ServerResponse.badRequest()::bodyValue)
    }
}
