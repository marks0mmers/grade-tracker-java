package com.marks0mmers.gradetracker.dto

import com.marks0mmers.gradetracker.constants.Role

data class UserDto(
        val id: String?,
        val username: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        var enabled: Boolean,
        var roles: List<Role>
)

data class CreateUserDto(
        val username: String,
        val password: String,
        val firstName: String,
        val lastName: String
)