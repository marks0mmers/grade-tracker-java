package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.dto.CourseDto
import com.marks0mmers.gradetracker.persistent.Course
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

@RestController
@RequestMapping("/api/courses")
class CourseController @Autowired constructor(
    private val courseRepository: CourseRepository,
    private val userService: UserService
) {

    @GetMapping
    fun getCoursesCurrentUser(p: Principal): Flux<CourseDto> = courseRepository
            .findAll()
            .filterWhen { c -> userService.currentUser(p).map { it?.id === c.userId } }
            .map { CourseDto(it) }

    @PostMapping
    fun createCourse(p: Principal, @RequestBody course: CourseDto): Mono<ResponseEntity<CourseDto>> {
        val userId = userService.currentUser(p).block()?.id
        return if (userId != null) {
            course.userId = userId
            courseRepository
                    .save(Course(course))
                    .map { ResponseEntity.ok(CourseDto(it)) }
        } else {
            Mono.just( ResponseEntity.badRequest().build() )
        }
    }

    @PutMapping("{id}")
    fun updateCourse(@PathVariable("id") courseId: String, @RequestBody course: CourseDto): Mono<ResponseEntity<CourseDto>> = courseRepository
            .findById(courseId)
            .flatMap {
                courseRepository.save(Course(it.id, course.title, course.description, course.section, course.creditHours, it.userId))
            }
            .map { ResponseEntity.ok( CourseDto(it) ) }
            .defaultIfEmpty( ResponseEntity.notFound().build() )

    @DeleteMapping("{id}")
    fun deleteCourse(@PathVariable("id") courseId: String): Mono<ResponseEntity<CourseDto>> {
        val courseToDelete = courseRepository.findById(courseId).block()
        return if (courseToDelete != null) courseRepository
                .delete(courseToDelete)
                .flatMap { Mono.just( ResponseEntity.ok(CourseDto(courseToDelete)) ) }
        else Mono.just( ResponseEntity.notFound().build() )
    }
}
