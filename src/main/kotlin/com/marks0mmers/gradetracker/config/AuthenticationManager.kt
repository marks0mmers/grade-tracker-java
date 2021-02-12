package com.marks0mmers.gradetracker.config

import com.marks0mmers.gradetracker.models.constants.Role
import com.marks0mmers.gradetracker.util.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager : ReactiveAuthenticationManager {

    @Autowired private lateinit var jwtUtil: JWTUtil

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val authToken = authentication?.credentials.toString()
        val username = jwtUtil.getUsernameFromToken(authToken) ?: return Mono.empty()
        return if (jwtUtil.validateToken(authToken)) {
            val claims = jwtUtil.getAllClaimsFromToken(authToken)
            val rolesMap = claims?.get("role", List::class.java)
            val roles = rolesMap?.map { Role.valueOf(it.toString()) }
            Mono.just(
                UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles?.map { SimpleGrantedAuthority(it.name) })
            )
        } else {
            Mono.empty()
        }
    }
}
