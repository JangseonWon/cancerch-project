package com.gcgenome.lims.service

import com.gcgenome.lims.dto.Page
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
        return ServerResponse.ok().bodyValue(
            listOf(
                Page().apply {
                    icon = "fa-chart-bar"
                    title = "Analysis"
                    uri = "/avoid-service/analysis.html"
                    order = "2"
                })
        )
    }
}