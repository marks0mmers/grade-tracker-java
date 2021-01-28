package com.marks0mmers.gradetracker.models.dto

import com.marks0mmers.gradetracker.models.persistent.Grade
import com.marks0mmers.gradetracker.util.panic

data class GradeDto(
        val id: String,
        val name: String,
        val grade: Double,
        val gradeCategoryId: String
) {
    constructor(g: Grade) : this(
        g.id ?: panic("Grade id is null"),
        g.name,
        g.grade,
        g.gradeCategoryId
    )
}