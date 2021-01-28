package com.marks0mmers.gradetracker.models.vm

data class CourseSubmissionVM(
    val title: String,
    val description: String,
    val section: Int,
    val creditHours: Int,
)