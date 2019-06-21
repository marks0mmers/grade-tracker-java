package com.marks0mmers.gradetracker.dto

import com.marks0mmers.gradetracker.persistent.Course

data class CourseDto(
        val id: String?,
        val title: String,
        val description: String,
        val section: Int,
        val creditHours: Int,
        var userId: String
) {
    constructor(course: Course): this(
            course.id,
            course.title,
            course.description,
            course.section,
            course.creditHours,
            course.userId
    )
}