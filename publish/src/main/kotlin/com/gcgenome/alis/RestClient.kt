package com.gcgenome.alis

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import reactor.core.publisher.Mono

@Service
class RestClient(private val client: RestTemplate) {
    fun fileUpload(fileUpload: FileUpload) : Boolean{
        val requestEntity = HttpEntity(fileUpload)
        requestEntity.headers.set("Content-Type", "application/json")
        val response = client.exchange("http://alis-api/alis/fileUpload", HttpMethod.PUT, requestEntity, String::class.java)
        return response.statusCode.is2xxSuccessful
    }
    fun state(request: Request, state: String, member: String, machine: String): Mono<Boolean> {
        val requestEntity = HttpEntity(request)
        requestEntity.headers.set("Content-Type", "application/json")
        val response = client.exchange("http://alis-api/alis/state/${state}/member/${member}/machine/${machine}/state", HttpMethod.PUT, requestEntity, String::class.java)
        return Mono.just(response.statusCode.is2xxSuccessful)
    }
    fun chkWorklist(request: Request) : Boolean {
        val requestEntity = HttpEntity(request)
        requestEntity.headers.set("Content-Type", "application/json")
        val response = client.exchange("http://alis-api/alis/chkWorklist", HttpMethod.PUT, requestEntity, String::class.java)
        return response.statusCode.is2xxSuccessful
    }
    fun cancelPublish(request: Request) : Boolean {
        val requestEntity = HttpEntity(request)
        requestEntity.headers.set("Content-Type", "application/json")
        val response = client.exchange("http://alis-api/alis/cancelPublish", HttpMethod.PUT, requestEntity, String::class.java)
        return response.statusCode.is2xxSuccessful
    }
}