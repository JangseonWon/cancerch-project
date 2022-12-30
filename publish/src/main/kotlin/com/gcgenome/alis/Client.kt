package com.gcgenome.alis

import com.gcgenome.alis.models.*
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

@Service
class Client(webClientBuilder: WebClient.Builder) {
    val webClient: WebClient = webClientBuilder.baseUrl("http://lims/alis-queue/").build()
    fun send(sample : Long, service : String, file : UUID, user : Authentication) : Mono<AlisResponse> {
        val date = LocalDate.parse(sample.toString().substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"))
        val subSample = sample.toString().substring(8).toLong()
        val publishInfo = PublishRequest(date, subSample, service)
        val requests = listOf(
            AlisRequest(
                fileId = file,
                operation = Operation.CREATE_IMG_DIV
            ),
            AlisRequest(
                fileId = file,
                operation = Operation.SEND_PDF
            ),
            AlisRequest(
                fileId = file,
                operation = Operation.CREATE_IMG_TOTAL
            ),
        )
        return webClient.post()
            .header("Content-Type", "application/json")
            .body(Mono.just(RequestBundle(UUID.randomUUID(), requests, user.principal as String, "Cancerch", publishInfo)), RequestBundle::class.java)
            .exchangeToMono{
                it.bodyToMono(AlisResponse::class.java)
            }
    }
}