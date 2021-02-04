package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.services.CourseAverageTrackingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.json

@Configuration
class CourseAverageTrackingController {

    @Autowired private lateinit var courseAverageTrackingService: CourseAverageTrackingService

    @Bean
    fun courseAverageTrackingRouter() = coRouter {
        "/api/v2".nest {
            GET("/courseAverageTracking/{courseId}") { req ->
                val courseId = req.pathVariable("courseId")
                val info = courseAverageTrackingService.getAllByCourse(courseId)
                ok().json()
                    .bodyAndAwait(info)
            }
        }
    }
}