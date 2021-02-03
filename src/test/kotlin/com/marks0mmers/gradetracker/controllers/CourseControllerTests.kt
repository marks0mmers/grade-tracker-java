package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.config.GlobalErrorHandler
import com.marks0mmers.gradetracker.config.PasswordEncoder
import com.marks0mmers.gradetracker.config.WebSecurityConfig
import com.marks0mmers.gradetracker.models.dto.CourseDto
import com.marks0mmers.gradetracker.models.persistent.Course
import com.marks0mmers.gradetracker.models.vm.CourseSubmissionVM
import com.marks0mmers.gradetracker.repositories.CourseRepository
import com.marks0mmers.gradetracker.repositories.SecurityContextRepository
import com.marks0mmers.gradetracker.repositories.UserRepository
import com.marks0mmers.gradetracker.services.CourseService
import com.marks0mmers.gradetracker.services.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [CourseController::class])
@Import(CourseService::class, UserService::class, PasswordEncoder::class)
class CourseControllerTests {

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var webClient: WebTestClient

    @Test
    fun testCreateCourse() {
        val course = Course(
            "CSC 316",
            "Data Science",
            3,
            3,
            ""
        )

        Mockito.`when`(courseRepository.save(course)).thenReturn(Mono.just(course))

        webClient
            .mutateWith(mockJwt())
            .post()
            .uri("/api/v2/courses")
            .contentType(APPLICATION_JSON)
            .bodyValue(CourseSubmissionVM(
                course.title,
                course.description,
                course.section,
                course.creditHours
            ))
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)

        Mockito.verify(courseRepository, times(1)).save(course)
    }
}