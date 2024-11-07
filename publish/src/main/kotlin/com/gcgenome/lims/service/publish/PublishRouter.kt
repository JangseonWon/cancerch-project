package com.gcgenome.lims.service.publish

import com.gcgenome.alis.models.AlisResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class PublishRouter(private val handler: PublishHandler) {
    @Bean("com.gcgenome.lims.service.publish.PublishRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        GET("/publish/publishoutcome",                                                      ::subscribe)
        PUT("/publish/samples/{sample}/services/{service}/reports/{createAt}/publish",              ::publishWithReport)

    }
    private fun publishWithReport(request: ServerRequest): Mono<ServerResponse>{
        val sample = request.pathVariable("sample").replace("-","").toLong()
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
