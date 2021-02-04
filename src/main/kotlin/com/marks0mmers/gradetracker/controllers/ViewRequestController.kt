package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.services.ViewRequestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

@Configuration
class ViewRequestController {

    @Autowired private lateinit var viewRequestService: ViewRequestService

    @Bean
    fun viewRequestRouter() = coRouter {
        "/api/v2/viewRequests".nest {
            GET("/forMeToRespond") { req ->
                val p = req.awaitPrincipal() ?: return@GET status(UNAUTHORIZED).buildAndAwait()
                val requests = viewRequestService.getAllForReceiver(p.name)
                ok().json()
                    .bodyAndAwait(requests)
            }

            GET("/sent") { req ->
                val p = req.awaitPrincipal() ?: return@GET status(UNAUTHORIZED).buildAndAwait()
                val requests = viewRequestService.getAllForRequester(p.name)
                ok().json()
                    .bodyAndAwait(requests)
            }

            POST("/send/user/{userId}") { req ->
                val p = req.awaitPrincipal() ?: return@POST status(UNAUTHORIZED).buildAndAwait()
                val userId = req.pathVariable("userId")
                val request = viewRequestService.sendUserViewRequest(p.name, userId)
                ok().json()
                    .bodyValueAndAwait(request)
            }

            POST("/approve/{requestId}") { req ->
                val p = req.awaitPrincipal() ?: return@POST status(UNAUTHORIZED).buildAndAwait()
                val requestId = req.pathVariable("requestId")
                val request = viewRequestService.approveViewRequest(requestId, p.name)
                ok().json()
                    .bodyValueAndAwait(request)
            }

            POST("/deny/{requestId}") { req ->
                val p = req.awaitPrincipal() ?: return@POST status(UNAUTHORIZED).buildAndAwait()
                val requestId = req.pathVariable("requestId")
                val request = viewRequestService.denyViewRequest(requestId, p.name)
                ok().json()
                    .bodyValueAndAwait(request)
            }
        }
    }
}