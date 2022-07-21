package com.gcgenome.lims.service.batch

import com.gcgenome.lims.data.Worklist
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class BatchRouter(private val handler: BatchHandler) {
    @Bean("com.greencross.lims.service.BatchRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        PUT("/worklist/{id}", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::save)
    }
    private fun save(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(object : ParameterizedTypeReference<List<Worklist>>(){})
            .flatMapMany { handler.saveMany(it) }
            .collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
}