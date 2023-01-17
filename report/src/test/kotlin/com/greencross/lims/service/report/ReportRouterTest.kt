package com.greencross.lims.service.report

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.io.File

@SpringBootTest // Springboot 띄움, applicationContext를 만듦
class ReportRouterTest{

    @Autowired
    lateinit var reportRouter: ReportRouter

    @Test
    @DisplayName("Log를 잘 가져오는가 테스트")
    fun logTest(){
        val client = WebTestClient.bindToRouterFunction(reportRouter.router()).build()
        val response = client.get()
            .uri("/samples/202208011715501/services/N201/log")
            .headers { it.set("X-USER-ID", "219926"); it.set("Content-Type", "application/vnd.avoid.v1+json") }
            .exchange()
            .expectBody()
            .returnResult().responseBody
        assertNotNull(String(response!!))
    }

    @Test
    @DisplayName("Pdf를 잘 가져오는가 테스트")
    fun logPdf(){
        val client = WebTestClient.bindToRouterFunction(reportRouter.router()).configureClient()
            .codecs { it.defaultCodecs().maxInMemorySize(-1) }.build()
        val response = client.get()
            .uri("/samples/202208011715501/services/N201/reportTotal")
            .headers { it.set("X-USER-ID", "219926"); it.set("Content-Type", "application/vnd.avoid.v1+json") }
            .exchange()
            .expectBody()
            .returnResult().responseBody
        File("Test.pdf").writeBytes(response!!)
    }
}