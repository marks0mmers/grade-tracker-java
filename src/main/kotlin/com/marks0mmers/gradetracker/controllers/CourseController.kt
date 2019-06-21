package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.dto.CourseDto
import com.marks0mmers.gradetracker.persistent.Course
import com.marks0mmers.gradetracker.repositories.CourseRepository
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
    private val courseRepository: CourseRepository,
    private val userService: UserService
) {

    @GetMapping
    fun getCoursesCurrentUser(p: Principal) = courseRepository
            .findAll()
            .filterWhen { c -> userService.currentUser(p).map { it?.id === c.userId } }
            .map { CourseDto(it) }

    @GetMapping("{id}")
    fun getCourseById(@PathVariable("id") courseId: String) = courseRepository
            .findById(courseId)
            .map { ResponseEntity.ok(CourseDto(it)) }
            .defaultIfEmpty( ResponseEntity.notFound().build() )

    @PostMapping
    fun createCourse(p: Principal, @RequestBody course: CourseDto) = userService
            .currentUser(p)
            .flatMap {
                    courseRepository
                            .save(Course(course, it.id ?: ""))
                            .map { c -> ResponseEntity.ok(CourseDto(c)) }

            }
            .defaultIfEmpty( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() )

    @PutMapping("{id}")
    fun updateCourse(@PathVariable("id") courseId: String, @RequestBody course: CourseDto) = courseRepository
            .findById(courseId)
            .flatMap {
                courseRepository.save(Course(it.id, course.title, course.description, course.section, course.creditHours, it.userId))
            }
            .map { ResponseEntity.ok( CourseDto(it) ) }
            .defaultIfEmpty( ResponseEntity.notFound().build() )

    @DeleteMapping("{id}")
    fun deleteCourse(@PathVariable("id") courseId: String) = courseRepository
            .findById(courseId)
            .flatMap {
                    courseRepository
                            .delete(it)
                    Mono.just(ResponseEntity.ok(CourseDto(it)))
            }
            .defaultIfEmpty( ResponseEntity.notFound().build() )
}
