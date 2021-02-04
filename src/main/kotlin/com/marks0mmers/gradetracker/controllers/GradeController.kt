package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.dto.GradeDto
import com.marks0mmers.gradetracker.models.vm.GradeSubmissionVM
import com.marks0mmers.gradetracker.services.GradeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class GradeController {

    @Autowired private lateinit var gradeService: GradeService

    @Bean
    fun gradeRouter() = coRouter {
        "/api/v2/grades".nest {
            GET("/gradeCategory/{gradeCategoryId}") { req ->
                val gradeCategoryId = req.pathVariable("gradeCategoryId")
                val grades = gradeService.getGradesFromCategory(gradeCategoryId)
                ok().json()
                    .bodyAndAwait(grades)
            }

            GET("/{gradeId}") { req ->
                val gradeId = req.pathVariable("gradeId")
                val grade = gradeService.getGrade(gradeId)
                ok().json()
                    .bodyValueAndAwait(grade)
            }

            POST("/gradeCategory/{gradeCategoryId}") { req ->
                val gradeCategoryId = req.pathVariable("gradeCategoryId")
                val gradeSubmission = req.awaitBody<GradeSubmissionVM>()
                val createdGrade = gradeService.newGrade(gradeCategoryId, gradeSubmission)
                ok().json()
                    .bodyValueAndAwait(createdGrade)
            }

            PUT("/{gradeId}") { req ->
                val gradeId = req.pathVariable("gradeId")
                val gradeUpdate = req.awaitBody<GradeDto>()
                val updatedGrade = gradeService.updateGrade(gradeId, gradeUpdate)
                ok().json()
                    .bodyValueAndAwait(updatedGrade)
            }

            DELETE("/{gradeId}") { req ->
                val gradeId = req.pathVariable("gradeId")
                val deletedGrade = gradeService.deleteGrade(gradeId)
                ok().json()
                    .bodyValueAndAwait(deletedGrade)
            }
        }
    }
}