package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.dto.CourseDto
import com.marks0mmers.gradetracker.persistent.Course
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.services.CourseService
import com.marks0mmers.gradetracker.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/api/courses")
class CourseController @Autowired constructor(
    private val courseService: CourseService
) {

    @GetMapping
    fun getCoursesCurrentUser(p: Principal) = courseService
            .getCoursesByUser(p.name)

    @GetMapping("{id}")
    fun getCourseById(@PathVariable("id") courseId: String) = courseService
            .getCourseById(courseId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty( ResponseEntity.notFound().build() )

    @PostMapping
    fun createCourse(p: Principal, @RequestBody course: CourseDto) = courseService
            .createCourse(p.name, course)

    @PutMapping("{id}")
    fun updateCourse(@PathVariable("id") courseId: String, @RequestBody course: CourseDto) = courseService
            .updateCourse(courseId, course)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty( ResponseEntity.notFound().build() )

    @DeleteMapping("{id}")
    fun deleteCourse(@PathVariable("id") courseId: String) = courseService
            .deleteCourse(courseId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty( ResponseEntity.notFound().build() )
}
