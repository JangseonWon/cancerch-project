package com.gcgenome.lims.service.worklist

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
    @Bean("com.greencross.lims.service.WorklistRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        GET("/worklist/search", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::search)
        GET("/worklist/batches/current", contentType(MediaType("application", "vnd.avoid.v1", Charsets.UTF_8)), ::current)
        GET("/worklist/batches/{batch}/max", contentType(MediaType("application", "vnd.avoid.v1", Charsets.UTF_8)), ::max)
    }
    private fun search(request: ServerRequest): Mono<ServerResponse>{
        return handler.search(searchParam(om, request.queryParams()))
            .flatMap { page->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", page.totalElements.toString())
                    .header("X-Total-Page", page.totalPages()?.toString())
                    .body(page.data, List::class.java)
            }.switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
           // .onErrorResume(Exception::class.java) { ServerResponse.badRequest().bodyValue(it.localizedMessage) }
    }
    // 가장 최근 생성된 Batch의 Batch Prefix를 돌려준다
    private fun current(request: ServerRequest): Mono<ServerResponse>{
        return Mono.empty();
    }
    // 주어진 Batch Prefix의 최대값을 돌려준다
    private fun max(request: ServerRequest): Mono<ServerResponse>{
        return Mono.empty();
    }
}