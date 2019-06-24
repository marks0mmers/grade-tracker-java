package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.dto.GradeCategoryDto
import com.marks0mmers.gradetracker.persistent.Grade
import com.marks0mmers.gradetracker.persistent.GradeCategory
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.repositories.GradeCategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GradeCategoryService @Autowired constructor(
        private val gradeCategoryRepository: GradeCategoryRepository,
        private val courseRepository: CourseRepository
) {

    fun getGradeCategoriesByCourse(courseId: String) = gradeCategoryRepository
            .findAll()
            .filter { it.courseId == courseId }
            .map { it.calculateGrades() }

    fun getAllForUser(userId: String) = courseRepository
            .findAll()
            .filter { it.userId == userId }
            .concatMap { getGradeCategoriesByCourse(it.id ?: "") }

    fun getById(id: String) = gradeCategoryRepository
            .findById(id)
            .map { it.calculateGrades() }

    fun create(gc: GradeCategoryDto) = gradeCategoryRepository
            .save(GradeCategory(gc))
            .map { it.calculateGrades() }

    fun update(gc: GradeCategoryDto) = gradeCategoryRepository
            .findById(gc.id ?: "")
            .flatMap { gradeCategoryRepository.save(GradeCategory(
                        it.id,
                        gc.title,
                        gc.percentage,
                        gc.numberOfGrades,
                        gc.courseId,
                        gc.grades.map { g -> Grade(g) }
            )) }
            .map { it.calculateGrades() }

    fun delete(id: String) = gradeCategoryRepository
            .findById(id)
            .flatMap {
                gradeCategoryRepository.delete(it)
                Mono.just(it)
            }
            .map { it.calculateGrades() }

}
