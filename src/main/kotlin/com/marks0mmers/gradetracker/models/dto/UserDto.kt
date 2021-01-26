package com.marks0mmers.gradetracker.models.dto

import com.marks0mmers.gradetracker.models.constants.Role
import com.marks0mmers.gradetracker.models.persistent.User

data class UserDto(
    val id: String?,
    val username: String,
    val firstName: String,
    val lastName: String,
    var enabled: Boolean,
    var roles: List<Role>,
    var token: String? = null
) {
    constructor(user: User) : this(
        user.id,
        user.username,
        user.firstName,
        user.lastName,
        user.enabled,
        user.roles
    )
}