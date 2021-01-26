package com.marks0mmers.gradetracker.models.vm

import com.marks0mmers.gradetracker.models.dto.GradeDto

data class UpdateGradeCategoryVM(
    val title: String,
    val percentage: Double,
    val numberOfGrades: Int,
    val courseId: String,
    val grades: List<GradeDto>,
    var id: String?
)