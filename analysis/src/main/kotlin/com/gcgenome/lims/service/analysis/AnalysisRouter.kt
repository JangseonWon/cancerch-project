package com.gcgenome.lims.service.analysis

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.data.Analysis
import com.gcgenome.lims.data.AnalysisResult
import com.gcgenome.lims.service.searchParam
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import reactor.core.publisher.Mono

@Configuration
class AnalysisRouter(
    private val handler: AnalysisHandler,
    private val om: ObjectMapper
) {
    @Bean("com.greencross.lims.service.AnalysisRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        GET("/analysis/search", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::search)
        PATCH("/analysis/{sample}/{service}/comment", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::updateComment)
        PATCH("/analysis/update", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::updateAll)
    }
    private fun search(request: ServerRequest): Mono<ServerResponse>{
        return handler.search(searchParam(om, request.queryParams()))
            .flatMap { page->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", page.totalElements.toString())
                    .header("X-Total-Page", page.totalPages()?.toString())
                    .body(page.data, List::class.java)
            }.switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
    private fun updateComment(request: ServerRequest): Mono<ServerResponse>{
        val sample = request.pathVariable("sample").toLong()
        val service = request.pathVariable("service")
        return  request.bodyToMono(String::class.java)
            .flatMapMany{ handler.updateComment(sample, service, it) }
            .then(ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.noContent().build())
    }
    private fun updateAll(request: ServerRequest): Mono<ServerResponse>{
        return request.bodyToFlux(Analysis::class.java)
            .flatMap{ handler.updateResult(it) }
            .then(ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.noContent().build())
    }
}