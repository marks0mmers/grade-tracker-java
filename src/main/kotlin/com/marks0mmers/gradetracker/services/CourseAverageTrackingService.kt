package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.models.dto.CourseAverageTrackingDto
import com.marks0mmers.gradetracker.models.persistent.CourseAverageTracking
import com.marks0mmers.gradetracker.repositories.CourseAverageTrackingRepository
import com.marks0mmers.gradetracker.util.Quadruple
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CourseAverageTrackingService {

    @Autowired
    private lateinit var courseAverageTrackingRepository: CourseAverageTrackingRepository

    @Autowired
    private lateinit var gradeCategoryService: GradeCategoryService

    fun getAllByCourse(courseId: String): Flow<CourseAverageTrackingDto> {
        return courseAverageTrackingRepository
            .findByCourseId(courseId)
            .asFlow()
            .map { CourseAverageTrackingDto(it) }
    }

    suspend fun addTrackingAverageOnChange(courseId: String): CourseAverageTrackingDto {
        val allCategories = gradeCategoryService
            .getGradeCategoriesByCourse(courseId)
            .toList(mutableListOf())

        val (
            currentAverage,
            potentialAverage,
            guarenteedAverage,
            percentComplete
        ) = allCategories.fold(Quadruple(0.0, 0.0, 0.0, 0.0)) { acc, gc ->
            acc.copy(
                a = acc.a + gc.currentAverage * gc.decimalPercentage,
                b = acc.b + gc.potentialAverage * gc.decimalPercentage,
                c = acc.c + gc.guarenteedAverage * gc.decimalPercentage,
                d = acc.d + (gc.grades.size.toDouble() / gc.numberOfGrades.toDouble()) * gc.decimalPercentage
            )
        }
        return courseAverageTrackingRepository
            .insert(CourseAverageTracking(
                Instant.now(),
                currentAverage,
                potentialAverage,
                guarenteedAverage,
                percentComplete,
                courseId
            ))
            .awaitFirst()
            .let { CourseAverageTrackingDto(it) }
    }
}