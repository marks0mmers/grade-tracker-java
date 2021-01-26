package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.vm.GradeCategorySubmissionVM
import com.marks0mmers.gradetracker.models.vm.UpdateGradeCategoryVM
import com.marks0mmers.gradetracker.services.GradeCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.*
import org.springframework.web.reactive.function.server.*

@Configuration
class GradeCategoryController {

    @Autowired
    private lateinit var gradeCategoryService: GradeCategoryService

    @Bean
    fun gradeCategoryRouter() = coRouter {
        "/api/v2".nest {
            GET("/gradeCategories") { req ->
                val p = req.awaitPrincipal() ?: return@GET status(UNAUTHORIZED).buildAndAwait()
                val gradeCategories = gradeCategoryService.getAllForUser(p.name)
                ok().json()
                    .bodyAndAwait(gradeCategories)
            }

            GET("/gradeCategories/course/{courseId}") { req ->
                val courseId = req.pathVariable("courseId")
                val gradeCategories = gradeCategoryService.getGradeCategoriesByCourse(courseId)
                ok().json()
                    .bodyAndAwait(gradeCategories)
            }

            GET("/gradeCategories/{gradeCategoryId}") { req ->
                val gradeCategoryId = req.pathVariable("gradeCategoryId")
                val gradeCategory = gradeCategoryService.getById(gradeCategoryId)
                ok().json()
                    .bodyValueAndAwait(gradeCategory)
            }

            POST("/gradeCategories/course/{courseId}") { req ->
                val courseId = req.pathVariable("courseId")
                val gradeCategory = req.awaitBody<GradeCategorySubmissionVM>()
                val createdCategory = gradeCategoryService.create(gradeCategory, courseId)
                ok().json()
                    .bodyValueAndAwait(createdCategory)
            }

            PUT("/gradeCategories/{gradeCategoryId}") { req ->
                val gradeCategoryId = req.pathVariable("gradeCategoryId")
                val gradeCategory = req.awaitBody<UpdateGradeCategoryVM>()
                gradeCategory.id = gradeCategoryId
                val updatedCategory = gradeCategoryService.update(gradeCategory)
                ok().json()
                    .bodyValueAndAwait(updatedCategory)
            }

            DELETE("/gradeCategories/{gradeCategoryId}") { req ->
                val gradeCategoryId = req.pathVariable("gradeCategoryId")
                val deletedCategory = gradeCategoryService.delete(gradeCategoryId)
                ok().json()
                    .bodyValueAndAwait(deletedCategory)
            }
        }
    }
}