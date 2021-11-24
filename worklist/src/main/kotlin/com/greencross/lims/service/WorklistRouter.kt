package com.greencross.lims.service

import com.greencross.lims.data.Worklist
import com.greencross.lims.entity.User
import com.greencross.lims.repo.SecurityContextRepository
import com.greencross.lims.data.Query_
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Configuration
open class WorklistRouter(private val handler: WorklistHandler) {
    @Bean
    open fun WorklistRouterInstance(): RouterFunction<ServerResponse?> {
        return RouterFunctions
            .route(RequestPredicates.POST("/worklist/list/{chkr}"), this::findAll)
            .andRoute(RequestPredicates.GET("/worklist/no"), this::getNo)
            .andRoute(RequestPredicates.GET("/worklist/changes"), this::subscribeWorklist)
            .andRoute(RequestPredicates.PUT("/worklist/save"), this::saveChanges)
            .andRoute(RequestPredicates.PATCH("/worklist/update"), this::updateChanges)
            .andRoute(RequestPredicates.PUT("/worklist/delete/{target}"), this::deleteWorklist)
            .andRoute(RequestPredicates.PATCH("/worklist/close/{target}"), this::closeWorklist)
    }
    private fun findAll(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(Query_::class.java)
            .flatMap { p->handler.list(p, request.pathVariable("chkr")) }
            .flatMap { page->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .header("X-Total-Count", page.totalElements.toString())
                    .header("X-Total-Page", page.totalPages().toString())
                    .header("X-Current-Page", page.currentPage.toString())
                    .body(page.data, Worklist::class.java)
            }.switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())
            .onErrorResume (ServerResponse.badRequest()::bodyValue)
    }
    private fun getNo(request: ServerRequest): Mono<ServerResponse> {
        return handler.getNo()
            .flatMap(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)::bodyValue)
            .switchIfEmpty(ServerResponse.noContent().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
    private fun saveChanges(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToFlux(Worklist::class.java)
            .flatMap{t->handler.save(t)}
            .then(ServerResponse.ok().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
    private fun updateChanges(request: ServerRequest): Mono<ServerResponse>{
        return request.bodyToFlux(Worklist::class.java)
            .flatMap{t->handler.update(t)}
            .then(ServerResponse.ok().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
    private fun closeWorklist(request: ServerRequest): Mono<ServerResponse>{
        return handler.close(request.pathVariable("target"))
            .then(ServerResponse.ok().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
    private fun deleteWorklist(request: ServerRequest): Mono<ServerResponse>{
        return handler.delete(request.pathVariable("target"))
            .then(ServerResponse.ok().build())
            .onErrorResume(ServerResponse.badRequest()::bodyValue)
    }
    private fun subscribeWorklist(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
            .body(BodyInserters.fromServerSentEvents(handler.subscribe().map{
                msg -> ServerSentEvent.builder<Worklist>(msg.data).event(msg.type.name).id(msg.data.id()).build()
            }))
   }
    private fun details(request: ServerRequest): Mono<User> {
        return request.principal()
            .cast(SecurityContextRepository.UserAuthentication::class.java)
            .map { auth->auth.details }
    }
}