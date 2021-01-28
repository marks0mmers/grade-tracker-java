package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.vm.CourseSubmissionVM
import com.marks0mmers.gradetracker.services.CourseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.*

@Configuration
class CourseController {

    @Autowired
    private lateinit var courseService: CourseService

    @Bean
    fun coursesRouter() = coRouter {
        "/api/v2".nest {
            GET("/courses") { req ->
                val p = req.awaitPrincipal() ?: return@GET status(UNAUTHORIZED).buildAndAwait()
                val courses = courseService.getCoursesByUser(p.name)
                ok().json()
                    .bodyAndAwait(courses)
            }

            GET("/courses/{id}") { req ->
                val courseId = req.pathVariable("id")
                val course = courseService.getCourseById(courseId)
                ok().json()
                    .bodyValueAndAwait(course)
            }

            POST("/courses") { req ->
                val p = req.awaitPrincipal() ?: return@POST status(UNAUTHORIZED).buildAndAwait()
                val course = req.awaitBody<CourseSubmissionVM>()
                val createdCourse = courseService.createCourse(p.name, course)
                ok().json()
                    .bodyValueAndAwait(createdCourse)
            }

            PUT("/courses/{id}") { req ->
                val courseId = req.pathVariable("id")
                val course = req.awaitBody<CourseSubmissionVM>()
                val updatedCourse = courseService.updateCourse(courseId, course)
                ok().json()
                    .bodyValueAndAwait(updatedCourse)
            }

            DELETE("/courses/{id}") { req ->
                val courseId = req.pathVariable("id")
                val deletedCourse = courseService.deleteCourse(courseId)
                ok().json()
                    .bodyValueAndAwait(deletedCourse)
            }
        }
    }
}
