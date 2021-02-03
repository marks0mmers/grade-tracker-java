package com.marks0mmers.gradetracker.models.dto

import com.marks0mmers.gradetracker.models.persistent.CourseAverageTracking
import com.marks0mmers.gradetracker.util.panic
import java.time.Instant

data class CourseAverageTrackingDto(
    val id: String,

    val dateTracked: Instant,

    val currentAverage: Double,

    val potentialAverage: Double,

    val guarenteedAverage: Double,

    val percentComplete: Double,

    val courseId: String
) {
    constructor(c: CourseAverageTracking) : this(
        c.id ?: panic("CourseAverageTracking ID is null"),
        c.dateTracked,
        c.currentAverage,
        c.potentialAverage,
        c.guarenteedAverage,
        c.percentComplete,
        c.courseId
    )
}