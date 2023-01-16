package com.gcgenome.lims.service.publish

import com.gcgenome.alis.models.AlisResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicate
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class PublishRouter(private val handler: PublishHandler) {
    private val contentType: RequestPredicate = RequestPredicates.contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8))
    @Bean("com.gcgenome.lims.service.publish.PublishRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
//        PUT("/samples/{sample}/services/{service}/publish", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::publish)
        GET("/samples/publishoutcome",                                                      ::subscribe)
        PUT("/samples/{sample}/services/{service}/reports/{createAt}/publish", contentType, ::publishWithReport)

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
    private fun subscribe(request: ServerRequest): Mono<ServerResponse>{
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
            .body(BodyInserters.fromServerSentEvents(handler.subscribe().map { msg ->
                ServerSentEvent.builder<AlisResponse>(msg).event(msg.publishModule).id(msg.identifier.toString()).build()
            }))
    }
}