package com.marks0mmers.gradetracker.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.marks0mmers.gradetracker.util.GradeTrackerException
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import io.jsonwebtoken.JwtException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebInputException

@Configuration
@Order(-2)
class GlobalErrorHandler : ErrorWebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val buffer = exchange.response.bufferFactory()
        val jsonEncoder = jacksonObjectMapper()
        exchange.response.statusCode = when (ex) {
            is GradeTrackerException -> ex.status
            is ServerWebInputException -> ex.status
            is ResponseStatusException -> ex.status
            is JwtException -> UNAUTHORIZED
            else -> INTERNAL_SERVER_ERROR
        }
        val bufferJson = when (ex) {
            is GradeTrackerException -> jsonEncoder.writeValueAsBytes(ex)
            is ServerWebInputException -> jsonEncoder.createObjectNode()
                .put("message", ex.reason)
                .put("cause", ex.cause.toString())
                .toString()
                .toByteArray()
            else -> jsonEncoder.createObjectNode()
                .put("message", ex.message)
                .put("cause", ex.cause.toString())
                .toString()
                .toByteArray()
        }
        exchange.response.headers.contentType = APPLICATION_JSON
        return exchange.response.writeWith(Mono.just(buffer.wrap(bufferJson)))
    }
}