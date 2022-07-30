package com.gcgenome.lims.service.sequencing

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class SequencingRouter(private val handler: SequencingHandler) {
    @Bean("SequencingRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        POST("/sequencing",          contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::sequencing)
    }
    private fun sequencing(request: ServerRequest): Mono<ServerResponse> = handler.sequencing(request.bodyToFlux(String::class.java)).flatMap { ServerResponse.ok().build() }
    // 워크리스트 배치 아이디가 없다
    // 워크리스트 내 검체 중 시퀀스가 없는게 있다
    // 워크리스트 내 검체끼리 시퀀스가 충돌한다
}