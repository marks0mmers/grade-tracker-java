package com.marks0mmers.gradetracker

import com.marks0mmers.gradetracker.config.AuthenticationManager
import com.marks0mmers.gradetracker.config.PBKDF2Encoder
import com.marks0mmers.gradetracker.controllers.CourseController
import com.marks0mmers.gradetracker.controllers.GradeCategoryController
import com.marks0mmers.gradetracker.controllers.UserController
import com.marks0mmers.gradetracker.controllers.ViewRequestController
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.repositories.SecurityContextRepository
import com.marks0mmers.gradetracker.services.CourseService
import com.marks0mmers.gradetracker.services.GradeCategoryService
import com.marks0mmers.gradetracker.services.UserService
import com.marks0mmers.gradetracker.services.ViewRequestService
import com.marks0mmers.gradetracker.util.JWTUtil
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.server.WebHandler
import reactor.core.publisher.Mono

@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class GradeTrackerApplication

fun main(args: Array<String>) {
	runApplication<GradeTrackerApplication>(*args)
}