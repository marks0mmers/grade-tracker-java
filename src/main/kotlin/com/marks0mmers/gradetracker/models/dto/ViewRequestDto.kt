package com.marks0mmers.gradetracker.models.dto

import com.marks0mmers.gradetracker.models.constants.ViewRequestStatus
import com.marks0mmers.gradetracker.models.persistent.ViewRequest
import com.marks0mmers.gradetracker.util.panic

data class ViewRequestDto(
    val id: String,
    val status: ViewRequestStatus,
    val requester: String,
    val receiver: String
) {
    constructor(viewRequest: ViewRequest) : this(
        viewRequest.id ?: panic("ViewRequest id is null"),
        ViewRequestStatus.values()[viewRequest.status],
        viewRequest.requester,
        viewRequest.receiver
    )
}