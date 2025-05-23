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
        PUT("/worklist/{id}/works", ::save)
    }
    private fun save(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(object : ParameterizedTypeReference<List<Preprocessing>>(){})
            .flatMapMany(handler::saveMany)
            .then(ServerResponse.ok().build())
            .doOnError(Exception::class.java) { it.printStackTrace() }
    }
}
