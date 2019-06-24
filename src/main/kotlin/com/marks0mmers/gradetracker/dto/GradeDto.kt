package com.marks0mmers.gradetracker.dto

import com.marks0mmers.gradetracker.persistent.Grade

data class GradeDto(
        val id: String?,
        val name: String,
        val grade: Double,
        val gradeCategoryId: String
) {
    constructor(g: Grade): this(
            g.id,
            g.name,
            g.grade,
            g.gradeCategoryId
    )
}