//package com.greencross.lims.service
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.web.reactive.function.server.router
//import org.springframework.http.MediaType
//import org.springframework.web.reactive.function.server.ServerRequest
//import org.springframework.web.reactive.function.server.ServerResponse
//import reactor.core.publisher.Mono
//
//@Configuration
//class ReportRouter(
//    private val handler: ReportHandler
//) {
//    @Bean("com.greencross.lims.service.ReportRouter")
//    fun router() = router{
//        PUT("/samples/{sample}/services/{service}/print", contentType(MediaType("application", "vnd.avoid.v1+json", Charsets.UTF_8)), ::print)
//    }
//    private fun print(request: ServerRequest): Mono<ServerResponse>{
//        val sample = request.pathVariable("sample")
//        val service = request.pathVariable("service")
//        return handler.print(sample, service)
//    }
//}