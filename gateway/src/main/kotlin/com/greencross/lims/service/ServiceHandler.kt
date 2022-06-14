package com.greencross.lims.service

import com.greencross.lims.dto.Page
import com.greencross.lims.dto.Service
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
import java.util.stream.Collectors
import java.util.stream.Stream

@Component
class ServiceHandler(
    private val discoveryClient: DiscoveryClient,
    private val client: WebClient.Builder
) {
    private val Log: Logger = LoggerFactory.getLogger(ServiceHandler::class.java)
    fun list(request: ServerRequest): Mono<Service> {
        return Flux.fromStream(discoveryClient.services.stream())
            .filter(Objects::nonNull)
            .flatMap { svc -> routes(request, svc) }
            .filter(Objects::nonNull)
            .collect(
                { Service() },
                { svc: Service, page: Page ->
                    if (svc.children() == null) svc.children(arrayOf(page))
                    else {
                        Log.info(page.toString())
                        val pages: List<Page> = Stream.concat(
                            Arrays.stream(svc.children()),
                            Stream.of(page))
                            .sorted(Comparator.comparing({ obj: Page -> obj.order() }, Comparator.nullsLast(Comparator.naturalOrder())))
                            .collect(Collectors.toList())
                        svc.children(pages.toTypedArray())
                    }
                }
            ).map { svc: Service -> svc.title("액체생검").order("C").prefix("/avoid-service") }
            .doOnNext{
                Log.info(it.toString())
            }
    }
    private fun routes(request: ServerRequest, service: String): Mono<Page> {
        Log.info("http://" + service)
        return client.baseUrl("http://" + service).build().get()
            .uri("/services")
            .headers{h->request.headers().asHttpHeaders().forEach(h::addAll)}
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Page::class.java)
            .timeout(Duration.ofMillis(500))
            .onErrorResume { Mono.empty()}
    }
}