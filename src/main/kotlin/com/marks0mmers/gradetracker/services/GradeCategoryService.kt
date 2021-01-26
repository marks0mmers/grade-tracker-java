package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.models.dto.GradeCategoryDto
import com.marks0mmers.gradetracker.models.persistent.Grade
import com.marks0mmers.gradetracker.models.persistent.GradeCategory
import com.marks0mmers.gradetracker.models.vm.GradeCategorySubmissionVM
import com.marks0mmers.gradetracker.models.vm.UpdateGradeCategoryVM
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.repositories.GradeCategoryRepository
import com.marks0mmers.gradetracker.util.panic
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GradeCategoryService {
    @Autowired
    private lateinit var gradeCategoryRepository: GradeCategoryRepository
    @Autowired
    private lateinit var courseRepository: CourseRepository
    @Autowired
    private lateinit var userService: UserService

    fun getGradeCategoriesByCourse(courseId: String): Flow<GradeCategoryDto> {
        return gradeCategoryRepository.findAll().asFlow()
            .filter { it.courseId == courseId }
            .map { it.calculateGrades() }
    }

    suspend fun getAllForUser(username: String): Flow<GradeCategoryDto> {
        val user = userService.findByUsername(username)
        return courseRepository.findAll().asFlow()
            .filter { it.userId == user.id && it.id != null }
            .transform { course ->
                emitAll(getGradeCategoriesByCourse(course.id!!))
            }
    }

    suspend fun getById(id: String): GradeCategoryDto {
        return gradeCategoryRepository
            .findById(id)
            .awaitFirstOrElse { panic("Cannot find grade category with id: $id") }
            .calculateGrades()
    }

    suspend fun create(gc: GradeCategorySubmissionVM, courseId: String): GradeCategoryDto {
        return gradeCategoryRepository
            .save(GradeCategory(gc, courseId))
            .awaitFirst()
            .calculateGrades()
    }

    suspend fun update(gc: UpdateGradeCategoryVM): GradeCategoryDto {
        return gc.id?.let { gradeCategoryId ->
            return gradeCategoryRepository
                .save(GradeCategory(
                    gradeCategoryId,
                    gc.title,
                    gc.percentage,
                    gc.numberOfGrades,
                    gc.courseId,
                    gc.grades.map { g -> Grade(g) }
                ))
                .awaitFirst()
                .calculateGrades()
        } ?: panic("Grade Category ID not set")
    }

    suspend fun delete(id: String): GradeCategoryDto {
        gradeCategoryRepository.deleteById(id).awaitFirst()
        return getById(id)
    }

}
