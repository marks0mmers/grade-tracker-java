package com.marks0mmers.gradetracker.models.persistent

import com.marks0mmers.gradetracker.models.dto.GradeCategoryDto
import com.marks0mmers.gradetracker.models.vm.GradeCategorySubmissionVM
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Document
data class GradeCategory(
    @NotNull val title: String,
    @Positive @Max(100)
    val percentage: Double,
    @Positive val numberOfGrades: Int,
    @NotNull val courseId: String,
    @DBRef val grades: List<Grade>
) {
    @Id var id: String? = null

    constructor(gc: GradeCategorySubmissionVM, courseId: String) : this(
        gc.title,
        gc.percentage,
        gc.numberOfGrades,
        courseId,
        emptyList()
    )

    constructor(gc: GradeCategoryDto) : this(
        gc.title,
        gc.percentage,
        gc.numberOfGrades,
        gc.courseId,
        gc.grades.map { Grade(it) }
    ) {
        this.id = gc.id
    }
}