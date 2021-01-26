package com.marks0mmers.gradetracker.models.vm

data class CreateUserVM(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String
)