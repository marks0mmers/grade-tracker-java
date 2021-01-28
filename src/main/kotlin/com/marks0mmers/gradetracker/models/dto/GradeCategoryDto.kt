package com.marks0mmers.gradetracker.models.dto

import com.marks0mmers.gradetracker.models.persistent.GradeCategory
import com.marks0mmers.gradetracker.util.panic

data class GradeCategoryDto(
    val id: String,
    val title: String,
    val percentage: Double,
    val numberOfGrades: Int,
    val courseId: String,
    val grades: List<GradeDto>
) {

    constructor(gc: GradeCategory) : this(
        gc.id ?: panic("GradeCategory id is null"),
        gc.title,
        gc.percentage,
        gc.numberOfGrades,
        gc.courseId,
        gc.grades.map { GradeDto(it) }
    )

    private val totalOfCurrentGrades: Double
        get() = grades.map { it.grade }.sum()

    val remainingGrades: Int
        get() = numberOfGrades - grades.size

    val currentAverage: Double
        get() = if (grades.isNotEmpty()) totalOfCurrentGrades / grades.size else totalOfCurrentGrades / 1

    val potentialAverage: Double
        get() = (totalOfCurrentGrades + 100 * remainingGrades) / numberOfGrades

    val guarenteedAverage: Double
        get() = totalOfCurrentGrades / numberOfGrades
}