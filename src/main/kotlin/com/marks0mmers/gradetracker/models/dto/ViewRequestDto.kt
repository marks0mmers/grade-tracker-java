package com.marks0mmers.gradetracker.models.dto

import com.marks0mmers.gradetracker.models.constants.ViewRequestStatus
import com.marks0mmers.gradetracker.models.persistent.ViewRequest

data class ViewRequestDto(
    val id: String?,
    val status: ViewRequestStatus,
    val requester: String,
    val receiver: String
) {
    constructor(viewRequest: ViewRequest) : this(
        viewRequest.id,
        ViewRequestStatus.values()[viewRequest.status],
        viewRequest.requester,
        viewRequest.receiver
    )
}