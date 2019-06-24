package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.dto.CourseDto
import com.marks0mmers.gradetracker.persistent.Course
import com.marks0mmers.gradetracker.repositories.CourseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CourseService @Autowired constructor(
        private val courseRepository: CourseRepository,
        private val userService: UserService
) {
    fun getCoursesByUser(username: String) = courseRepository
            .findAll()
            .filterWhen { c -> userService.findByUsername(username).map { it.id == c.userId } }
            .map { CourseDto(it) }

    fun getCourseById(courseId: String) = courseRepository
            .findById(courseId)
            .map { CourseDto(it) }

    fun createCourse(username: String, course: CourseDto) = userService
            .findByUsername(username)
            .flatMap {
                    courseRepository
                            .save(Course(course, it.id ?: ""))
                            .map { c -> CourseDto(c) }
            }

    fun updateCourse(courseId: String, course: CourseDto) = courseRepository
            .findById(courseId)
            .flatMap {
                courseRepository.save(Course(it.id, course.title, course.description, course.section, course.creditHours, it.userId))
            }
            .map { CourseDto(it) }

    fun deleteCourse(courseId: String) = courseRepository
            .findById(courseId)
            .flatMap {
                courseRepository.delete(it)
                Mono.just(it)
            }
            .map { CourseDto(it) }
}