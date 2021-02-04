package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.config.PasswordEncoder
import com.marks0mmers.gradetracker.models.constants.Role
import com.marks0mmers.gradetracker.models.dto.CourseDto
import com.marks0mmers.gradetracker.models.persistent.Course
import com.marks0mmers.gradetracker.models.persistent.User
import com.marks0mmers.gradetracker.models.vm.CourseSubmissionVM
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.repositories.UserRepository
import com.marks0mmers.gradetracker.services.CourseService
import com.marks0mmers.gradetracker.services.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [CourseController::class])
@Import(CourseService::class, UserService::class, PasswordEncoder::class)
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(PER_CLASS)
class CourseControllerTests {

    @MockBean lateinit var userRepository: UserRepository
    @MockBean lateinit var courseRepository: CourseRepository
    @Autowired lateinit var webClient: WebTestClient

    private val user = User(
        "marks0mmers",
        "",
        "Mark",
        "Sommers",
        true,
        listOf(Role.ROLE_USER)
    ).also { it.id = "UserID" }

    private val course = Course(
        "CSC 316",
        "Data Science",
        3,
        3,
        "UserID"
    ).also { it.id = "CourseID" }

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(csrf())
        `when`(userRepository.findByUsername(anyString())).thenReturn(user.toMono())
    }

    @Test
    fun testGetAllCourses() {
        val courses = listOf(course)
        `when`(courseRepository.findAll()).thenReturn(courses.toFlux())

        webClient.get()
            .uri("/api/v2/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
    }

    @Test
    fun testGetCourseById() {
        `when`(courseRepository.findById(anyString())).thenReturn(course.toMono())

        webClient.get()
            .uri("/api/v2/courses/${course.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)
    }

    @Test
    fun testCreateCourse() {
        `when`(courseRepository.insert(any<Course>())).thenReturn(course.toMono())

        webClient.post()
            .uri("/api/v2/courses")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                CourseSubmissionVM(
                    course.title,
                    course.description,
                    course.section,
                    course.creditHours
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)

        verify(courseRepository).insert(course)
    }

    @Test
    fun testUpdateCourse() {
        `when`(courseRepository.findById(anyString())).thenReturn(course.toMono())
        `when`(courseRepository.save(any())).thenReturn(course.copy(title = "CSC 316 Edit").also { it.id = course.id }
            .toMono())

        webClient.put()
            .uri("/api/v2/courses/${course.id}")
            .contentType(APPLICATION_JSON)
            .bodyValue(
                CourseSubmissionVM(
                    course.title + " Edit",
                    course.description,
                    course.section,
                    course.creditHours
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)

        verify(courseRepository).save(course.copy(title = "CSC 316 Edit").also { it.id = course.id })
    }

    @Test
    fun testDeleteCourse() {
        `when`(courseRepository.findById(anyString())).thenReturn(course.toMono())
        `when`(courseRepository.deleteById(anyString())).thenReturn(Mono.empty())

        webClient.delete()
            .uri("/api/v2/courses/${course.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)

        verify(courseRepository).deleteById(course.id!!)
    }
}