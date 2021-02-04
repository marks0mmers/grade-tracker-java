package com.marks0mmers.gradetracker.models.persistent

import com.marks0mmers.gradetracker.models.dto.GradeDto
import com.marks0mmers.gradetracker.models.vm.GradeSubmissionVM
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Document
data class Grade(
    @NotNull var name: String,
    @PositiveOrZero @Max(100)
    var grade: Double,
    @NotNull val gradeCategoryId: String
) {
    @Id var id: String? = null

    constructor(g: GradeSubmissionVM, gradeCategoryId: String) : this(
        g.name,
        g.grade,
        gradeCategoryId
    )

    constructor(g: GradeDto) : this(
        g.name,
        g.grade,
        g.gradeCategoryId
    ) {
        this.id = g.id
    }
}