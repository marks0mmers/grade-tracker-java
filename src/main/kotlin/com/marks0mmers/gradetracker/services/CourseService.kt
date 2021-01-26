package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.models.dto.CourseDto
import com.marks0mmers.gradetracker.models.persistent.Course
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.util.panic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CourseService {

    @Autowired
    private lateinit var courseRepository: CourseRepository
    @Autowired
    private lateinit var userService: UserService

    fun getCoursesByUser(username: String): Flow<CourseDto> {
        return courseRepository.findAll().asFlow()
            .transform { c ->
                if (userService.findByUsername(username).id == c.userId) {
                    emit(c)
                }
            }
            .map { CourseDto(it) }
    }

    suspend fun getCourseById(courseId: String): CourseDto {
        return courseRepository
            .findById(courseId)
            .awaitFirstOrElse { panic("Cannot find course with ID: $courseId") }
            .let { CourseDto(it) }
    }

    suspend fun createCourse(username: String, course: CourseDto): CourseDto {
        val user = userService.findByUsername(username)
        return courseRepository
            .save(Course(course, user.id ?: panic("User doesn't have ID")))
            .awaitFirst()
            .let { CourseDto(it) }
    }

    suspend fun updateCourse(courseId: String, courseDto: CourseDto): CourseDto {
        val course = getCourseById(courseId)
        return courseRepository
            .save(
                Course(
                    course.id,
                    courseDto.title,
                    courseDto.description,
                    courseDto.section,
                    courseDto.creditHours,
                    course.userId ?: ""
                )
            )
            .awaitFirst()
            .let { CourseDto(it) }
    }

    suspend fun deleteCourse(courseId: String): CourseDto {
        courseRepository.deleteById(courseId).awaitFirst()
        return getCourseById(courseId)
    }
}