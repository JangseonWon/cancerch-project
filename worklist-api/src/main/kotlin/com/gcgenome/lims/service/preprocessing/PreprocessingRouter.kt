package com.gcgenome.lims.service.preprocessing

import com.gcgenome.lims.data.Preprocessing
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class PreprocessingRouter(private val handler: PreprocessingHandler) {
    @Bean("com.greencross.lims.service.PreprocessingRouter")
    fun router() = org.springframework.web.reactive.function.server.router {
        PUT("/worklist/{id}/works", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::save)
    }
    private fun save(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(object : ParameterizedTypeReference<List<Preprocessing>>(){})
            .flatMapMany { handler.saveMany(it) }
            .collectList()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
    }
}