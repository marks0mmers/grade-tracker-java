package com.marks0mmers.gradetracker.config

import com.marks0mmers.gradetracker.util.GradeTrackerException
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
@Order(-2)
class GlobalErrorHandler : ErrorWebExceptionHandler {
    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val buffer = exchange.response.bufferFactory()
        if (ex is GradeTrackerException) {
            exchange.response.statusCode = ex.status
            exchange.response.headers.contentType = TEXT_PLAIN
            return exchange.response.writeWith(Mono.just(buffer.wrap(ex.message?.toByteArray() ?: "".toByteArray())))
        }
        exchange.response.statusCode = INTERNAL_SERVER_ERROR
        exchange.response.headers.contentType = TEXT_PLAIN
        return exchange.response.writeWith(Mono.just(buffer.wrap("Unknown error".toByteArray())))
    }
}