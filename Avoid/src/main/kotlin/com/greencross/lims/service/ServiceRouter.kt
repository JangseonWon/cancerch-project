package com.greencross.lims.service

import com.greencross.lims.dto.Page
import com.greencross.lims.repo.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
open class ServiceRouter(
    var repo: UserRepository
) {
    @Bean
    open fun serviceRouterInstance(): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.GET("/services"), this::services)
    }

    private fun services(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().bodyValue(Page().icon("fa-cubes")
            .title("Avoid")
            .uri("/RnD-service/avoid.html").order("1"))
    }
}