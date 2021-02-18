package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.dto.GradeDto
import com.marks0mmers.gradetracker.models.vm.GradeSubmissionVM
import com.marks0mmers.gradetracker.services.GradeService
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [GradeController::class])
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GradeControllerTests {

    @MockBean lateinit var gradeService: GradeService
    @Autowired lateinit var webClient: WebTestClient

    private val grade = GradeDto(
        "GradeID",
        "Homework 1",
        100.0,
        "GradeCategoryID"
    )

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(SecurityMockServerConfigurers.csrf())
    }

    @Test
    fun `test getting grades by grade category`() {
        `when`(gradeService.getGradesFromCategory(grade.gradeCategoryId)).thenReturn(flowOf(grade))

        webClient.get()
            .uri("/api/v2/grades/gradeCategory/${grade.gradeCategoryId}")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(GradeDto::class.java)
    }

    @Test
    fun `test getting grade by id`(): Unit = runBlocking {
        `when`(gradeService.getGrade(grade.id)).thenReturn(grade)

        webClient.get()
            .uri("/api/v2/grades/${grade.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeDto::class.java)
    }

    @Test
    fun `test creating grade`(): Unit = runBlocking {
        val gradeSubmission = GradeSubmissionVM(grade.name, grade.grade)
        `when`(gradeService.newGrade(grade.gradeCategoryId, gradeSubmission)).thenReturn(grade)

        webClient.post()
            .uri("/api/v2/grades/gradeCategory/${grade.gradeCategoryId}")
            .accept(APPLICATION_JSON)
            .bodyValue(gradeSubmission)
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeDto::class.java)

        verify(gradeService).newGrade(grade.gradeCategoryId, gradeSubmission)
    }

    @Test
    fun `test updating grade`(): Unit = runBlocking {
        val gradeEdit = grade.copy(name = "${grade.name} Edit")
        `when`(gradeService.updateGrade(grade.id, gradeEdit)).thenReturn(gradeEdit)

        webClient.put()
            .uri("/api/v2/grades/${grade.id}")
            .accept(APPLICATION_JSON)
            .bodyValue(gradeEdit)
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeDto::class.java)

        verify(gradeService).updateGrade(grade.id, gradeEdit)
    }

    @Test
    fun `test deleting grade`(): Unit = runBlocking {
        `when`(gradeService.deleteGrade(grade.id)).thenReturn(grade)

        webClient.delete()
            .uri("/api/v2/grades/${grade.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeDto::class.java)

        verify(gradeService).deleteGrade(grade.id)
    }
}