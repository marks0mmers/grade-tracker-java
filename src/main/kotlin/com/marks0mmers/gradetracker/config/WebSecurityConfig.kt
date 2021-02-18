package com.marks0mmers.gradetracker.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.*
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig {

    @Autowired private lateinit var authenticationManger: AuthenticationManager
    @Autowired private lateinit var authenticationConverter: JwtServerAuthenticationConverter

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val authenticationFilter = AuthenticationWebFilter(authenticationManger).apply {
            setServerAuthenticationConverter(authenticationConverter)
        }

        return http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .exceptionHandling()
                .authenticationEntryPoint { swe, _ -> Mono.fromRunnable { swe.response.statusCode = UNAUTHORIZED } }
                .accessDeniedHandler { swe, _ -> Mono.fromRunnable { swe.response.statusCode = FORBIDDEN } }
            .and()
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(HttpMethod.POST, "/api/v2/users", "/api/v2/users/login").permitAll()
                .anyExchange().authenticated()
            .and().build()
    }
}