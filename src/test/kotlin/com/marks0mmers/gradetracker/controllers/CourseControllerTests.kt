package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.dto.CourseDto
import com.marks0mmers.gradetracker.models.vm.CourseSubmissionVM
import com.marks0mmers.gradetracker.services.CourseService
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [CourseController::class])
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(PER_CLASS)
class CourseControllerTests {
    @MockBean lateinit var courseService: CourseService
    @Autowired lateinit var webClient: WebTestClient

    private val course = CourseDto(
        "CourseID",
        "CSC 316",
        "Data Science",
        3,
        3,
        "UserID"
    )

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(csrf())
    }

    @Test
    fun testGetAllCourses() {
        val courses = listOf(course)
        `when`(courseService.getCoursesByUser(anyString())).thenReturn(courses.asFlow())

        webClient.get()
            .uri("/api/v2/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDto::class.java)
    }

    @Test
    fun testGetCourseById(): Unit = runBlocking {
        `when`(courseService.getCourseById(anyString())).thenReturn(course)

        webClient.get()
            .uri("/api/v2/courses/${course.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)
    }

    @Test
    fun testCreateCourse(): Unit = runBlocking {
        val courseSubmission = CourseSubmissionVM(
                course.title,
                course.description,
                course.section,
                course.creditHours
            )
        `when`(courseService.createCourse("marks0mmers", courseSubmission)).thenReturn(course)

        webClient.post()
            .uri("/api/v2/courses")
            .contentType(APPLICATION_JSON)
            .bodyValue(courseSubmission)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)

        verify(courseService).createCourse("marks0mmers", courseSubmission)
    }

    @Test
    fun testUpdateCourse(): Unit = runBlocking {
        val courseEdit = CourseSubmissionVM(
            course.title + " Edit",
            course.description,
            course.section,
            course.creditHours
        )

        `when`(courseService.updateCourse(course.id, courseEdit)).thenReturn(course)
        webClient.put()
            .uri("/api/v2/courses/${course.id}")
            .contentType(APPLICATION_JSON)
            .bodyValue(courseEdit)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)

        verify(courseService).updateCourse(course.id, courseEdit)
    }

    @Test
    fun testDeleteCourse(): Unit = runBlocking {
        `when`(courseService.deleteCourse(anyString())).thenReturn(course)

        webClient.delete()
            .uri("/api/v2/courses/${course.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDto::class.java)

        verify(courseService).deleteCourse(course.id)
    }
}