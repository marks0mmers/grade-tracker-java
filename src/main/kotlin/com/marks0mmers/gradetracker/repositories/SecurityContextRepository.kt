package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.config.AuthenticationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository : ServerSecurityContextRepository {

    @Autowired private lateinit var authenticationManager: AuthenticationManager

    override fun save(p0: ServerWebExchange?, p1: SecurityContext?): Mono<Void> {
        return Mono.empty()
    }

    override fun load(swe: ServerWebExchange?): Mono<SecurityContext> {

        swe?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION)?.let { authHeader ->
            if (authHeader.startsWith("Bearer ")) {
                val authToken = authHeader.substring(7)
                val auth = UsernamePasswordAuthenticationToken(authToken, authToken)
                return authenticationManager
                    .authenticate(auth)
                    .map { SecurityContextImpl(it) }
            }
        }
        return Mono.empty()
    }
}
