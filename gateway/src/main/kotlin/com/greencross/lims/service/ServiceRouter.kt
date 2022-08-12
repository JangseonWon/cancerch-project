package com.greencross.lims.service

import com.gcgenome.lims.test.Avoid
import com.gcgenome.lims.test.HasCode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.function.Function
import java.util.stream.Collectors

@Configuration
open class ServiceRouter(private var handler: ServiceHandler) {
    @Bean
    open fun router() = router {
        GET("/services", ::services)
        GET("/services/{service}", contentType(MediaType("application", "vnd.lims.v1", Charsets.UTF_8)), ::service2)
    }
    private fun services(request: ServerRequest): Mono<ServerResponse> {
        return handler.list(request)
            .flatMap { e-> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(e) }
            .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
            .onErrorResume (ServerResponse.badRequest()::bodyValue)
    }
    private val services: Map<String, Any> = (Avoid.values()).stream().collect(Collectors.toMap(HasCode::code, Function.identity()))
    private fun service2(request: ServerRequest): Mono<ServerResponse> {
        return services[request.pathVariable("service")].toMono()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty (ServerResponse.status(HttpStatus.NOT_FOUND).build())
            .doOnError { e -> e.printStackTrace() }
    }
}