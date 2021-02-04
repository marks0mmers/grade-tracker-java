package com.marks0mmers.gradetracker.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Profile("logger")
@Component
@Order(-999)
class RequestLoggingFilter : WebFilter {

    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType
        return ">>>REQUEST>>> $method $path $ACCEPT: $acceptableMediaTypes ${CONTENT_TYPE}: $contentType"
    }

    fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val statusCode = getStatus(response)
        val contentType = response.headers.contentType
        return "<<<RESPONSE<<< $method $path HTTP${statusCode.value()} ${statusCode.reasonPhrase} ${CONTENT_TYPE}: $contentType"
    }

    private fun getStatus(response: ServerHttpResponse): HttpStatus {
        return try {
            response.statusCode ?: HttpStatus.CONTINUE
        } catch (ex: Exception) {
            HttpStatus.CONTINUE
        }
    }

    override fun filter(p0: ServerWebExchange, p1: WebFilterChain): Mono<Void> {
        logger.info(getRequestMessage(p0))
        val filter = p1.filter(p0)
        p0.response.beforeCommit {
            logger.info(getResponseMessage(p0))
            Mono.empty()
        }
        return filter
    }

}