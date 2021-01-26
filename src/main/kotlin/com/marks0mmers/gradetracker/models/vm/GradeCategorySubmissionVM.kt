package com.marks0mmers.gradetracker.models.vm

data class GradeCategorySubmissionVM(
    val title: String,
    val percentage: Double,
    val numberOfGrades: Int,
)