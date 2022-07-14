package com.gcgenome.alis

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class Client(webClientBuilder: WebClient.Builder) {
    val uri = "http://alis/"
    val webClient: WebClient = webClientBuilder.build()
    fun fileUpload(fileUpload: FileUpload) : Boolean{
        return webClient.put().uri(uri+"fileUpload")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(fileUpload))
            .exchangeToMono{
                if(it.statusCode().is2xxSuccessful) Mono.just(true)
            else Mono.just(false)
            }.block()!!
    }
    fun state(request: Request, state: String, member: String?, machine: String?): Mono<Boolean> {
        return webClient.put().uri(uri+"state/${state}/member/${member}/machine/${machine}/state")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(request))
            .exchangeToMono{
                if(it.statusCode().is2xxSuccessful) Mono.just(true)
                else Mono.just(false)
            }
    }
    fun chkWorklist(request: Request) : Boolean {
       return webClient.put().uri(uri+"chkWorklist")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(request))
            .exchangeToMono{
                if(it.statusCode().is2xxSuccessful) Mono.just(true)
                else Mono.just(false)
            }.block()!!
    }
    fun cancelPublish(request: Request) : Boolean {
        return webClient.put().uri(uri+"cancelPublish")
            .headers{ it.add("Content-Type", "application/json") }
            .body(BodyInserters.fromValue(request))
            .exchangeToMono{
                if(it.statusCode().is2xxSuccessful) Mono.just(true)
                else Mono.just(false)
            }.block()!!
    }
}