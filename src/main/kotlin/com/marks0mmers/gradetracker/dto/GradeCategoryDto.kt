package com.marks0mmers.gradetracker.dto

data class GradeCategoryDto(
        val id: String?,
        val title: String,
        val percentage: Double,
        val numberOfGrades: Int,
        val remainingGrades: Int,
        val courseId: String,
        val currentAverage: Double,
        val guarenteedAverage: Double,
        val potentialAverage: Double,
        val grades: List<GradeDto>
)