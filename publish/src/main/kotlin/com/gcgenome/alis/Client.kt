package com.gcgenome.alis

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class Client(webClientBuilder: WebClient.Builder) {
    val webClient: WebClient = webClientBuilder.baseUrl("http://alis-api/alis/").build()
    fun fileUpload(fileUpload: FileUpload) : Mono<Boolean>{
        return webClient.put().uri("fileUpload")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(fileUpload))
            .exchangeToMono{
                Mono.just(it.statusCode().is2xxSuccessful)
            }
    }
    fun state(request: Request, state: String, member: String?, machine: String?): Mono<Boolean> {
        return webClient.put().uri("state/${state}/member/${member}/machine/${machine}/state")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(request))
            .exchangeToMono{
                Mono.just(it.statusCode().is2xxSuccessful)
            }
    }
    fun chkWorklist(request: Request) : Mono<Boolean>{
       return webClient.put().uri("chkWorklist")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(request))
            .exchangeToMono{
                Mono.just(it.statusCode().is2xxSuccessful)
            }
    }
    fun cancelPublish(request: Request) : Mono<Boolean> {
        return webClient.put().uri("cancelPublish")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(request))
            .exchangeToMono{
                Mono.just(it.statusCode().is2xxSuccessful)
            }
    }
}