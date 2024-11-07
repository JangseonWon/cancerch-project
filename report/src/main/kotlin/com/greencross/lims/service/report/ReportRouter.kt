package com.greencross.lims.service.report

import com.gcgenome.report.versions.log.Log
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
class ReportRouter(
    private val handler: ReportHandler
) {
    @Bean("com.greencross.lims.service.Report.ReportRouter")
    fun router() = router{
        GET("/report/queue",                                                                                 ::subscribe)
        GET("/report/works",                                                                                 ::works)
        GET("/report/samples/{sample}/services/{service}/log",                                               ::getLog)
        GET("/report/samples/{sample}/services/{service}/reportTotal",                                       ::reportTotal)
        PUT("/report/samples/{sample}/services/{service}/batch/{batch}/row/{row}/print/{lang}",              ::print)
        GET("/report/samples/{sample}/services/{service}/reports/{createAt}",                                ::preview)
    }
    private fun getLog(request: ServerRequest): Mono<ServerResponse> {
        val (sample, service)  = request.pathVariable("sample").replace("-", "") to request.pathVariable("service")
        return Mono.just(ServerResponse.ok()).flatMap {
            it.body(handler.getLogs(sample.replace("-", "").toLong(), service), Log::class.java)
        }.onErrorResume { ServerResponse.badRequest().body(Mono.just("Bad Request, check path parameters")) }
    }

    private fun reportTotal(request: ServerRequest): Mono<ServerResponse> {
        val (sample, service)  = request.pathVariable("sample").replace("-", "") to request.pathVariable("service")
        return Mono.just(ServerResponse.ok()).flatMap {
            it.body(handler.getReports(sample.replace("-", "").toLong(), service), ByteArray::class.java)
        }.onErrorResume { ServerResponse.badRequest().body(Mono.just("Bad Request, check path parameters")) }
    }

    private fun print(request: ServerRequest): Mono<ServerResponse>{
        val sample  = request.pathVariable("sample").replace("-", "")
        val service = request.pathVariable("service")
        val lang    = request.pathVariable("lang")
        val batch   = request.pathVariable("batch")
        val row     = request.pathVariable("row").toLong()
        return request.bodyToMono(String::class.java)
            .switchIfEmpty(Mono.just("최초보고"))
            .flatMap { handler.print(sample.toLong(), service, batch, row, lang, it) }
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
