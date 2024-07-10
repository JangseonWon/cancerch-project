package com.gcgenome.lims.service.analysis

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.lims.data.Analysis
import com.gcgenome.lims.data.LinkRequest
import com.gcgenome.lims.service.searchParam
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import reactor.core.publisher.Mono

@Configuration
class AnalysisRouter(
    private val handler: AnalysisHandler,
    private val om: ObjectMapper
) {
    private val contentType: RequestPredicate = contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8))
    @Bean("com.greencross.lims.service.AnalysisRouter")
    fun router() = router{
        GET("/analysis/search",                                    contentType, ::search)
        GET("/analysis/validate/{sample}/{service}/{batch}/{row}", contentType, ::linkCheck)
        GET("/analysis/request/{sample}/{service}",                contentType, ::requestCheck)
        POST("/analysis/linkData",                                 contentType, ::linkData)
        PATCH("/analysis/{sample}/{service}/{batch}/{row}/comment",contentType, ::updateComment)
        PATCH("/analysis/update",                                  contentType, ::updateAll)
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
        val batch = request.pathVariable("batch")
        val row = request.pathVariable("row").toInt()
        return  request.bodyToMono(String::class.java)
            .flatMapMany{ handler.updateComment(sample, service, batch, row, it) }
            .then(ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.noContent().build())
    }
    private fun updateAll(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux(Analysis::class.java)
            .flatMap{ handler.updateResult(it) }
            .then(ServerResponse.ok().build())
            .switchIfEmpty(ServerResponse.noContent().build())
    }
    private fun linkData(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(LinkRequest::class.java)
            .flatMap(handler::linkData)
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.badRequest().bodyValue("잘못된 요청입니다."))
    }
    private fun requestCheck(request: ServerRequest): Mono<ServerResponse> {
        val sample = request.pathVariable("sample").toLong()
        val service = request.pathVariable("service")
        return handler.chkRequest(sample, service)
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.badRequest().bodyValue("잘못된 요청입니다."))
    }
    private fun linkCheck(request: ServerRequest): Mono<ServerResponse> {
        val sample = request.pathVariable("sample").toLong()
        val service = request.pathVariable("service")
        val batch = request.pathVariable("batch")
        val row = request.pathVariable("row").toInt()
        return handler.chkAnalysis(sample, service, batch, row)
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.badRequest().bodyValue("잘못된 요청입니다."))
    }
}
