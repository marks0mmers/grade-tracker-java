package com.marks0mmers.gradetracker.models.persistent

import org.hibernate.validator.constraints.Range
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank

@Document
data class ViewRequest (
    @NotBlank
    @Range(min = 0, max = 2)
    var status: Int,

    @NotBlank
    val requester: String,

    @NotBlank
    val receiver: String
) {
    @Id
    val id: String? = null
}