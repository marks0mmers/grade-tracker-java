package com.marks0mmers.gradetracker.persistent

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Document
data class Course(
        @Id
        val id: String?,

        @NotBlank
        val title: String,

        @NotBlank
        val description: String,

        @Positive
        val section: Int,

        @Positive @Max(4)
        val creditHours: Int,

        val userId: String
)