package com.marks0mmers.gradetracker.models.persistent

import java.time.Instant
import javax.validation.constraints.PositiveOrZero

data class CourseAverageTracking(
    val dateTracked: Instant,

    @PositiveOrZero
    val currentAverage: Double,

    @PositiveOrZero
    val potentialAverage: Double,

    @PositiveOrZero
    val guarenteedAverage: Double,

    @PositiveOrZero
    val percentComplete: Double,

    val courseId: String
) {
    var id: String? = null
}
