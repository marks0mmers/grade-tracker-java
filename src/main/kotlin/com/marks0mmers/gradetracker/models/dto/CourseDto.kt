package com.marks0mmers.gradetracker.models.dto

import com.marks0mmers.gradetracker.models.persistent.Course
import com.marks0mmers.gradetracker.util.panic

data class CourseDto(
    val id: String,
    val title: String,
    val description: String,
    val section: Int,
    val creditHours: Int,
    var userId: String?
) {
    constructor(course: Course) : this(
        course.id ?: panic("Course id is null"),
        course.title,
        course.description,
        course.section,
        course.creditHours,
        course.userId
    )
}