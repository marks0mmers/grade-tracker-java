package com.marks0mmers.gradetracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class GradeTrackerApplication

fun main(args: Array<String>) {
	runApplication<GradeTrackerApplication>(*args)
}