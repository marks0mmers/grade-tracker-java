package com.marks0mmers.gradetracker.persistent

import com.marks0mmers.gradetracker.dto.GradeCategoryDto
import com.marks0mmers.gradetracker.dto.GradeDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Document
data class GradeCategory(
        @Id
        val id: String?,

        @NotNull
        val title: String,

        @Positive @Max(100)
        val percentage: Double,

        @Positive
        val numberOfGrades: Int,

        @NotNull
        val courseId: String,

        @DBRef
        val grades: List<Grade>
) {
        constructor(gc: GradeCategoryDto): this(
                gc.id,
                gc.title,
                gc.percentage,
                gc.numberOfGrades,
                gc.courseId,
                gc.grades.map { Grade(it) }
        )

        fun calculateGrades(): GradeCategoryDto {
                val totalOfCurrentGrades = grades.map { it.grade }.sum()
                val remainingGrades = numberOfGrades - grades.size
                val currentAverage = if (grades.isNotEmpty()) totalOfCurrentGrades / grades.size else totalOfCurrentGrades / 1
                val potentialAverage = (totalOfCurrentGrades + 100 * remainingGrades) / numberOfGrades
                val guarenteedAverage = totalOfCurrentGrades / numberOfGrades
                return GradeCategoryDto(
                        id,
                        title,
                        percentage,
                        numberOfGrades,
                        remainingGrades,
                        courseId,
                        currentAverage,
                        potentialAverage,
                        guarenteedAverage,
                        grades.map { GradeDto(it) }
                )
        }
}