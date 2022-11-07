package com.greencross.lims.service.report

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.RequestPredicates.contentType
import reactor.core.publisher.Mono
import java.util.*

@Configuration
class ReportRouter(
    private val handler: ReportHandler
) {
    private val contentType: RequestPredicate = contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8))
        @Bean("com.greencross.lims.service.Report.ReportRouter")
    fun router() = router{
        GET("/samples/queue",                                                                         ::subscribe)
        GET("/samples/works",                                                            contentType, ::works)
        PUT("/samples/{sample}/services/{service}/batch/{batch}/row/{row}/print/{lang}", contentType, ::print)
        GET("/samples/{sample}/services/{service}/reports/{createAt}"                  , contentType(MediaType("application", "vnd.avoid.v1", Charsets.UTF_8)), ::preview)

    }
    private fun print(request: ServerRequest): Mono<ServerResponse>{
        val sample  = request.pathVariable("sample")
        val service = request.pathVariable("service")
        val lang    = request.pathVariable("lang")
        val batch   = request.pathVariable("batch")
        val row     = request.pathVariable("row")
        return handler.print(sample.toLong(), service, batch, row, lang)
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
    private fun preview(request: ServerRequest): Mono<ServerResponse>{
        val sample      = request.pathVariable("sample").toString().replace("-", "")
        val service     = request.pathVariable("service")
        val createAt    = request.pathVariable("createAt")
        return handler.preview(sample.toLong(), service, createAt.toLong())
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)::bodyValue)
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
    private fun subscribe(request: ServerRequest): Mono<ServerResponse>{
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
            .body(BodyInserters.fromServerSentEvents(handler.subscribe().map { msg ->
                ServerSentEvent.builder<com.gcgenome.lims.data.Report>(msg.data).event(msg.type.name)
                    .id(msg.data.sample().toString()+"$"+msg.data.service()+"$"+msg.data.createAt()).build()
            }))
    }
    private fun works(request: ServerRequest): Mono<ServerResponse>{
        return handler.works()
            .collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
    }
}