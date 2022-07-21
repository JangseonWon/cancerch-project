package com.greencross.lims.service

import com.gcgenome.lims.service.Menu
import com.gcgenome.lims.service.Page
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@Component
class ServiceHandler(
    private val discoveryClient: DiscoveryClient,
    private val client: WebClient.Builder
) {
    private val Log: Logger = LoggerFactory.getLogger(ServiceHandler::class.java)
    fun list(request: ServerRequest): Mono<Menu> {
        return Flux.fromStream(discoveryClient.services.stream())
            .filter(Objects::nonNull)
            .flatMap { svc -> routes(request, svc).flatMapMany { Flux.fromArray(it) } }
            .filter(Objects::nonNull)
            .collect(
                { Menu("액체생검", "C", "/avoid-service", mutableListOf()) },
                { svc, page -> svc.children.add(page) }
            ).map { it.apply { it.children.sortWith(compareBy(nullsLast()){ order })} }
    }
    private fun routes(request: ServerRequest, service: String): Mono<Array<Page>> {
        Log.info("http://$service")
        return client.baseUrl("http://$service").build().get()
            .uri("/services")
            .headers{h->request.headers().asHttpHeaders().forEach(h::addAll)}
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Array<Page>::class.java)
            .timeout(Duration.ofMillis(500))
            .onErrorResume { Mono.empty()}
    }
}