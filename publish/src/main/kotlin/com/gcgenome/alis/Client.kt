package com.gcgenome.alis

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.alis.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpCookie
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.util.MultiValueMapAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class Client (
    @Qualifier("httpWebClient")
    private val webClient: WebClient
){
    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun send(sample : Long, service : String, file : UUID, user : Authentication, serverRequest : ServerRequest) : Mono<Boolean> {
        val date = LocalDate.parse(sample.toString().substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"))
        val subSample = sample.toString().substring(8).toLong()
        val publishInfo = PublishRequest(date, subSample, service)
        val cookieMap = MultiValueMapAdapter(serverRequest.cookies().map { it.key to it.value.map(HttpCookie::getValue) }.toMap())
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
        return webClient
            .post()
            .uri("https://lims/alis-queue/async")
            .cookies{it.addAll(cookieMap)}
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(listOf(RequestBundle(UUID.randomUUID(), requests, user.principal as String, "Cancerch", publishInfo))), List::class.java)
            .exchangeToMono{
                Mono.just(it.statusCode().is2xxSuccessful)
            }
    }
}