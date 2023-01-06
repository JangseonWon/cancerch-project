package com.gcgenome.lims.service.report

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "PT100S")
internal class SimpleTest{

    @Autowired
    lateinit var client : WebTestClient
    private val requests = listOf(
        "/samples/202210049713001/services/N201/reports/1667540964442/publish",
        )

    @Test
    fun test(){
        requests.forEach{
            client.put()
                .uri(it)
                .contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8))
                .header("X-USER-ID", "221943")
                .exchange()
                .expectStatus()
                .is2xxSuccessful
        }
    }



}

