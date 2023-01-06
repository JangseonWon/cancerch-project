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
        "/samples/202210049713009/services/N201/reports/1667540964620/publish",
        )

    @Test
    fun test(){
        requests.forEach{
            client.put()
                .uri(it)
                .cookie("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJuYmYiOjE2NzI5Njc4MTAsImV4cCI6MTY3MzAwMzgxMCwiaXNzIjoibGltcy5nY2dlbm9tZS5jb20iLCJhdWQiOiJsaW1zLmdjZ2Vub21lLmNvbSIsImlhdCI6MTY3Mjk2NzgxMCwianRpIjoiZGY4OTljMmQtOWQzOC00NjA5LWFhZjctODRiZTJhN2YyYWRhIiwiaWQiOiIyMjE5MzEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwibmFtZSI6IuuvvOuPme2drCIsImRlcGFydG1lbnQiOiJMSU1TIiwiZW1haWwiOiJtaW5kaEBnY2NvcnAuY29tIn0.aokK4YBxF912o0TRmjZhPhq0nv_x1YfJqLR1wfngDAplynk9l14VAq8-B68gU0edWEuRGrTNA1bIFTKaa5REp-Xcv9KjLlSiyxAZ1wRwOfapBQhCUGdKCHxgENCKKBH8DdVXI787_ADEgH7gq0pYJgSi57SuXndPyqhRPWTWc6mVvu65DaX2iRYTuE78ic15dtT8Ut1LtgJonO2liyklL7T2HMkQbbp-CUSIoSzSFn6tzX__kQrmjFdJDiebl7dAxF-0mlE5OaMsDXwc8Qh-12ENbFnKrWYj9rwOrfcV-Ip_yu3XC7BSZm_mHbjQ5jgCkFzBNhNcQhz75OpKacWjYu-yDnqHG0gJabboktaElSwpWyKPh-EpC-hJEFCeKUbnpSvO7CJG1w71G9jmDVOBNuuFAjh5EuKC9CAwGwv6lanG6AU0QFutwoH8AxEQmgU-gWHbn2HENmH-U41KOupxcIHxUgY5AxN8Ozg7mNdL3BGZqX6vnIDljbuRM9GZbNVS9zr5LylmHFOsfv8MONaD3uwZ5K_B3f7xWsa3sOVab6-IDvedx4u0wgIcPlgdP4z0jZssn11givFemXcRLzD6nmAKvJpUZRcgSONGieVmXfLvSYd7KSpMt-OMpESHmEdedEUEvWuoOsG7ws-NMyiIDFTgmw5uiZr1Fc2r0HwXfaQ")
                .contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8))
                .header("X-USER-ID", "221943")
                .exchange()
                .expectStatus()
                .is2xxSuccessful
        }
    }



}

