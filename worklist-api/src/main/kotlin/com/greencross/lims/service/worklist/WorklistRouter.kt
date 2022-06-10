package com.greencross.lims.service.worklist

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.service.searchParam
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class WorklistRouter(private val handler: WorklistHandler, private val om: ObjectMapper) {
    @Bean("com.greencross.lims.service.Router")
    fun router() = org.springframework.web.reactive.function.server.router {
        POST("/worklist/search", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::search)
    }
    private fun search(request: ServerRequest): Mono<ServerResponse>{
        return handler.search(searchParam(om, request.queryParams()))
            .flatMap { page->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", page.totalElements.toString())
                    .header("X-Total-Page", page.totalPages()?.toString())
                    .body(page.data, List::class.java)
            }.switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
            .onErrorResume(Exception::class.java) { ServerResponse.badRequest().bodyValue(it.localizedMessage) }
    }
}