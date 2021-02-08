package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.dto.CourseAverageTrackingDto
import com.marks0mmers.gradetracker.services.CourseAverageTrackingService
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Instant

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [CourseAverageTrackingController::class])
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseAverageTrackingControllerTests {

    @MockBean lateinit var courseAverageTrackingService: CourseAverageTrackingService
    @Autowired lateinit var webClient: WebTestClient

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(SecurityMockServerConfigurers.csrf())
    }

    @Test
    fun testGetCourseAverageTrackingByCourse() {
        `when`(courseAverageTrackingService.getAllByCourse("CourseID")).thenReturn(flowOf(CourseAverageTrackingDto(
            "CourseAverageTrackingID",
            Instant.now(),
            72.0,
            90.2,
            55.2,
            60.6,
            "CourseID"
        )))

        webClient.get()
            .uri("/api/v2/courseAverageTracking/CourseID")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseAverageTrackingDto::class.java)
    }
}