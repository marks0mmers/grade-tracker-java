package com.marks0mmers.gradetracker.util

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

class GradeTrackerException(message: String?, val status: HttpStatus) : Exception(message)

fun panic(message: String?): Nothing = throw GradeTrackerException(message, status = BAD_REQUEST)
fun unauthorizedPanic(message: String?): Nothing = throw GradeTrackerException(message, status = UNAUTHORIZED)