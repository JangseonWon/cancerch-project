package com.greencross.lims.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.greencross.lims.dto.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ReportHandler(
    private val om: ObjectMapper
) {
    fun report(): Flux<User>{
        return Flux.just(User())
    }
}