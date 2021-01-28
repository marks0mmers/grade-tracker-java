package com.marks0mmers.gradetracker.config

import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Component
class CORSFilter : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
    }
}