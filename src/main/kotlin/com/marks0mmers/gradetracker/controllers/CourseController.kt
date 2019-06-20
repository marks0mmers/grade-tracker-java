package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.dto.CourseDto
import com.marks0mmers.gradetracker.persistent.Course
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/courses")
class CourseController @Autowired constructor(
    private val courseRepository: CourseRepository,
    private val userService: UserService
) {

    @GetMapping
    fun getCoursesCurrentUser(p: Principal) = courseRepository
            .findAll()
            .filter { it.userId == userService.currentUser(p).block()?.id ?: false }
            .map { CourseDto(it) }

    @PostMapping
    fun createCourse(p: Principal, @RequestBody course: CourseDto) = courseRepository
            .save(Course(course))
            .map { CourseDto(it) }

}
