package com.gcgenome.alis.service

import com.gcgenome.alis.data.FileUpload
import com.gcgenome.alis.data.Request
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
@Configuration
class Router(private val handler: Handler) {
    @Bean("com.gcgenome.lims.service.alis.ProcedureRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        PUT("/alis/fileUpload", contentType(MediaType("application", "json", Charsets.UTF_8)), ::fileUpload)
        PUT("/alis/state/{state}/member/{member}/machine/{machine}/state", contentType(MediaType("application", "json", Charsets.UTF_8)), ::state)
        PUT("/alis/cancelPublish", contentType(MediaType("application", "json", Charsets.UTF_8)), ::cancelPublish)
        PUT("/alis/chkWorklist", contentType(MediaType("application", "json", Charsets.UTF_8)), ::chkWorklist)
    }

    private fun state(request: ServerRequest): Mono<ServerResponse>{
        println("State")
        val state = request.pathVariable("state")
        val member = request.pathVariable("member")
        val machine = request.pathVariable("machine")
        return request.bodyToMono(Request::class.java)
            .flatMap {
                handler.state(it, state, member, machine)
            }
            .flatMap { if(it) ServerResponse.ok().build() else ServerResponse.badRequest().build()}

    }

    private fun fileUpload(request: ServerRequest): Mono<ServerResponse>{
        println("fileUpload")
        return request.bodyToMono(FileUpload::class.java)
            .publishOn(Schedulers.boundedElastic())
            .map(handler::fileUpload)
            .publishOn(Schedulers.parallel())
            .flatMap { if(it) ServerResponse.ok().build() else ServerResponse.badRequest().build()}
    }

    private fun cancelPublish(request: ServerRequest): Mono<ServerResponse>{
        println("cancelPublish")
        return request.bodyToMono(Request::class.java)
            .publishOn(Schedulers.boundedElastic())
            .map(handler::cancelPublish)
            .publishOn(Schedulers.parallel())
            .flatMap { if(it) ServerResponse.ok().build() else ServerResponse.badRequest().build()}
    }

    private fun chkWorklist(request: ServerRequest): Mono<ServerResponse>{
        println("chkWorklist")
        return request.bodyToMono(Request::class.java)
            .publishOn(Schedulers.boundedElastic())
            .map(handler::chkWorklist)
            .publishOn(Schedulers.parallel())
            .flatMap { if(it) ServerResponse.ok().build() else ServerResponse.badRequest().build()}
    }
}