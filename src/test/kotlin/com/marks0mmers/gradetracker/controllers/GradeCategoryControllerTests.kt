package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.dto.GradeCategoryDto
import com.marks0mmers.gradetracker.models.vm.GradeCategorySubmissionVM
import com.marks0mmers.gradetracker.services.GradeCategoryService
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
@WebFluxTest(controllers = [GradeCategoryController::class])
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GradeCategoryControllerTests {

    @MockBean lateinit var gradeCategoryService: GradeCategoryService
    @Autowired lateinit var webClient: WebTestClient

    private val gradeCategory = GradeCategoryDto(
        "GradeCategoryID",
        "Homework",
        20.0,
        10,
        "CourseID",
        listOf()
    )

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(SecurityMockServerConfigurers.csrf())
    }

    @Test
    fun testGetGradeCategoriesForUser(): Unit = runBlocking {
        `when`(gradeCategoryService.getAllForUser("marks0mmers")).thenReturn(flowOf(gradeCategory))

        webClient.get()
            .uri("/api/v2/gradeCategories")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(GradeCategoryDto::class.java)
    }

    @Test
    fun testGetGradeCategoriesByCourse() {
        `when`(gradeCategoryService.getGradeCategoriesByCourse(gradeCategory.courseId)).thenReturn(flowOf(gradeCategory))

        webClient.get()
            .uri("/api/v2/gradeCategories/course/${gradeCategory.courseId}")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(GradeCategoryDto::class.java)
    }

    @Test
    fun testGetGradeCategoryById(): Unit = runBlocking {
        `when`(gradeCategoryService.getById(gradeCategory.id)).thenReturn(gradeCategory)

        webClient.get()
            .uri("/api/v2/gradeCategories/${gradeCategory.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeCategoryDto::class.java)
    }

    @Test
    fun testCreateGradeCategory(): Unit = runBlocking {
        val gcSubmission = GradeCategorySubmissionVM(
            "Homework",
            20.0,
            10
        )

        `when`(gradeCategoryService.create(gcSubmission, gradeCategory.courseId)).thenReturn(gradeCategory)

        webClient.post()
            .uri("/api/v2/gradeCategories/course/${gradeCategory.courseId}")
            .accept(APPLICATION_JSON)
            .bodyValue(gcSubmission)
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeCategoryDto::class.java)

        verify(gradeCategoryService).create(gcSubmission, gradeCategory.courseId)
    }

    @Test
    fun testUpdateGradeCategory(): Unit = runBlocking {
        val edit = gradeCategory.copy(title = "${gradeCategory.title} Edit")

        `when`(gradeCategoryService.update(edit, gradeCategory.id)).thenReturn(edit)

        webClient.put()
            .uri("/api/v2/gradeCategories/${gradeCategory.id}")
            .accept(APPLICATION_JSON)
            .bodyValue(edit)
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeCategoryDto::class.java)

        verify(gradeCategoryService).update(edit, gradeCategory.id)
    }

    @Test
    fun testDeleteGradeCategory(): Unit = runBlocking {
        `when`(gradeCategoryService.delete(gradeCategory.id)).thenReturn(gradeCategory)

        webClient.delete()
            .uri("/api/v2/gradeCategories/${gradeCategory.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(GradeCategoryDto::class.java)

        verify(gradeCategoryService).delete(gradeCategory.id)
    }
}