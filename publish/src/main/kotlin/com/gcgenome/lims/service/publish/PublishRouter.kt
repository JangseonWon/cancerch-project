package com.gcgenome.lims.service.publish

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Configuration
class PublishRouter(private val handler: PublishHandler) {
    @Bean("com.gcgenome.lims.service.publish.PublishRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
//        PUT("/samples/{sample}/services/{service}/publish", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::publish)
        PUT("/samples/{sample}/services/{service}/reports/{createAt}/publish", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::publishWithReport)
    }
    private fun publishWithReport(request: ServerRequest): Mono<ServerResponse>{
        val sample = request.pathVariable("sample").toLong()
        val service = request.pathVariable("service")
        val createAt = request.pathVariable("createAt").toLong()
        return handler.publish(sample, service, createAt, request).flatMap{
                if(it) ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build()
                else ServerResponse.status(HttpStatus.NO_CONTENT).build()
            }
    }
//    private fun publishToSender(request: ServerRequest): Mono<ServerResponse>{
//        val sample = request.pathVariable("sample").toLong()
//        val service = request.pathVariable("service")
//        val createAt = request.pathVariable("createAt").toLong()
//
//        return handler.publish2(sample, service, createAt).flatMap{
//            if(it) ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build()
//            else ServerResponse.status(HttpStatus.NO_CONTENT).build()
//        }
//    }
}