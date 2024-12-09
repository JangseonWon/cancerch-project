package com.gcgenome.alis

import com.fasterxml.jackson.databind.ObjectMapper
import com.gcgenome.alis.models.*
import com.gcgenome.lims.model.ResultInfo
import com.gcgenome.lims.projection.Request
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpCookie
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMapAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class Client(
    @Qualifier("httpWebClient")
    private val webClient: WebClient
) {
    private val logger = LoggerFactory.getLogger(Client::class.java)

    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun send(
        request: Request,
        file: UUID,
        user: Authentication,
        serverRequest: ServerRequest,
        resultInfo: ResultInfo
    ): Mono<Boolean> {
        val date = LocalDate.parse(request.sample.toString().substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"))
        val subSample = request.sample.toString().substring(8).toLong()
        val publishInfo = PublishRequest(date, subSample, request.service)
        val cookieMap =
            MultiValueMapAdapter(serverRequest.cookies().map { it.key to it.value.map(HttpCookie::getValue) }.toMap())
        val requests = mutableListOf(
            AlisRequest(
                fileId = file.toString(),
                operation = Operation.CREATE_IMG_DIV
            ),
            AlisRequest(
                fileId = file.toString(),
                operation = Operation.SEND_PDF
            ),
            AlisRequest(
                fileId = file.toString(),
                operation = Operation.CREATE_IMG_TOTAL
            ),
            AlisRequest(
                fileId = file.toString(),
                operation = Operation.SEND_RESULT,
                results = listOf(
                    AlisResult(
                        request.service + "010",
                        if(isKangbukRequest(request)) convertResultKangbuk(resultInfo.result)
                        else resultInfo.result,
                        resultInfo.resultType,
                        resultInfo.comment?:""
                    )
                )
            ),
            AlisRequest(
                fileId = file.toString(),
                operation = Operation.SEND_INTERPRETATION,
                textShort = "<![CDATA[${resultInfo.textReport}]]>",
                text = "<![CDATA[${resultInfo.textReport}]]>"
            )
        )

        val response = webClient
            .post()
            .uri("https://lims/alis-queue/async")
            .cookies { it.addAll(cookieMap) }
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                Mono.just(
                    listOf(
                        RequestBundle(
                            UUID.randomUUID(),
                            requests,
                            user.principal as String,
                            "Cancerch",
                            publishInfo
                        )
                    )
                ), List::class.java
            )
            .exchangeToMono {
                logger.info(it.statusCode().toString())
                Mono.just(it.statusCode().is2xxSuccessful)
            }
        return response
    }

    private fun convertResultKangbuk(result: String) = when(result) {
        "일반관리" -> "미검출"
        "관심관리" -> "검출_관심관리"
        "집중관리" -> "검출_집중관리"
        else -> throw Exception("의도치 않은 문구가 삽입됐습니다.")
    }

    private fun isKangbukRequest(request: Request) : Boolean = request.institution!! == "G062546" || request.institution == "G075003" || request.institution == "G001125" || request.institution == "Q000003" || request.institution == "G0Q0006" || request.institution == "G044110" || request.institution == "G044111"
}
