package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.config.AuthenticationManger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.lang.UnsupportedOperationException

@Component
class SecurityContextRepository @Autowired constructor(
        val authenticationManager: AuthenticationManger
) : ServerSecurityContextRepository {
    override fun save(p0: ServerWebExchange?, p1: SecurityContext?): Mono<Void> {
        throw UnsupportedOperationException("Not Supported Yet.")
    }

    override fun load(swe: ServerWebExchange?): Mono<SecurityContext> {
        val request = swe?.request
        val authHeader = request?.headers?.getFirst(HttpHeaders.AUTHORIZATION)
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val authToken = authHeader.substring(7)
            val auth = UsernamePasswordAuthenticationToken(authToken, authToken)
            authenticationManager
                    .authenticate(auth)
                    .map { SecurityContextImpl(it) }
        } else {
            Mono.empty()
        }
    }
}
