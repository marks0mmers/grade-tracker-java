package com.marks0mmers.gradetracker.models.persistent

import com.marks0mmers.gradetracker.models.dto.ViewRequestDto
import org.hibernate.validator.constraints.Range
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank

@Document
data class ViewRequest (
    @Id
    val id: String?,

    @NotBlank
    @Range(min = 0, max = 2)
    var status: Int,

    @NotBlank
    val requester: String,

    @NotBlank
    val receiver: String
) {
    constructor(viewRequest: ViewRequestDto): this(
        viewRequest.id,
        viewRequest.status.ordinal,
        viewRequest.requester,
        viewRequest.receiver
    )
}