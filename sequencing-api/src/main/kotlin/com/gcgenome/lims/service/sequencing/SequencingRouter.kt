package com.gcgenome.lims.service.sequencing

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Configuration
class SequencingRouter(private val handler: SequencingHandler) {
    @Bean("SequencingRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        POST("/sequencing",          contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::sequencing)
    }
    private fun sequencing(request: ServerRequest): Mono<ServerResponse> =
        handler.sequencing(request.bodyToMono(String::class.java)
            .flatMapMany { str-> Flux.fromIterable(str.split(",")) })
            .flatMap { ServerResponse.ok().build() }
            .doOnError { it.printStackTrace() }
            .onErrorResume(Exception::class.java) { ServerResponse.badRequest().bodyValue(it.localizedMessage) }
}