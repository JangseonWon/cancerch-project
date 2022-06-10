package com.greencross.lims.service

import com.greencross.lims.dto.Page
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
class ServiceRouter {
    @Bean
    fun serviceRouterInstance(): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.GET("/services"), this::services)
    }
    private fun services(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().bodyValue(Page().icon("fa-braille")
            .title("Worklist")
            .uri("/avoid-service/worklist.html").order("1"))
    }
}