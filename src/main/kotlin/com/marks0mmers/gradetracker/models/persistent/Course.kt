package com.marks0mmers.gradetracker.models.persistent

import com.marks0mmers.gradetracker.models.dto.CourseDto
import com.marks0mmers.gradetracker.models.vm.CourseSubmissionVM
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Document
data class Course(
    @NotBlank val title: String,
    @NotBlank val description: String,
    @Positive val section: Int,
    @Positive @Max(4)
    val creditHours: Int,
    val userId: String
) {
    @Id var id: String? = null

    constructor(course: CourseSubmissionVM, userId: String) : this(
        course.title,
        course.description,
        course.section,
        course.creditHours,
        userId
    )

    constructor(course: CourseDto) : this(
        course.title,
        course.description,
        course.section,
        course.creditHours,
        course.userId!!
    ) {
        this.id = course.id
    }
}