package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.constants.ViewRequestStatus
import com.marks0mmers.gradetracker.models.dto.ViewRequestDto
import com.marks0mmers.gradetracker.services.ViewRequestService
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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [ViewRequestController::class])
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ViewRequestControllerTests {

    @MockBean lateinit var viewRequestService: ViewRequestService
    @Autowired lateinit var webClient: WebTestClient

    private val viewRequest = ViewRequestDto(
        "ViewRequestID",
        ViewRequestStatus.SENT,
        "marks0mmers",
        "marks0mmers"
    )

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(SecurityMockServerConfigurers.csrf())
    }

    @Test
    fun `test getting view requests for user to respond`(): Unit = runBlocking {
        `when`(viewRequestService.getAllForReceiver(viewRequest.receiver)).thenReturn(flowOf(viewRequest))

        webClient.get()
            .uri("/api/v2/viewRequests/forMeToRespond")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ViewRequestDto::class.java)
    }

    @Test
    fun `test getting view requests that user sent`(): Unit = runBlocking {
        `when`(viewRequestService.getAllForRequester(viewRequest.requester)).thenReturn(flowOf(viewRequest))

        webClient.get()
            .uri("/api/v2/viewRequests/sent")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(ViewRequestDto::class.java)
    }

    @Test
    fun `test sending view requests`(): Unit = runBlocking {
        `when`(viewRequestService.sendUserViewRequest(viewRequest.requester, viewRequest.receiver)).thenReturn(
            viewRequest
        )

        webClient.post()
            .uri("/api/v2/viewRequests/send/user/${viewRequest.receiver}")
            .exchange()
            .expectStatus().isOk
            .expectBody(ViewRequestDto::class.java)

        verify(viewRequestService).sendUserViewRequest(viewRequest.requester, viewRequest.receiver)
    }

    @Test
    fun `test approving view requests`(): Unit = runBlocking {
        `when`(
            viewRequestService.approveViewRequest(
                viewRequest.id,
                viewRequest.requester
            )
        ).thenReturn(viewRequest.copy(status = ViewRequestStatus.APPROVED))

        webClient.post()
            .uri("/api/v2/viewRequests/approve/${viewRequest.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(ViewRequestDto::class.java)

        verify(viewRequestService).approveViewRequest(viewRequest.id, viewRequest.requester)
    }

    @Test
    fun `test denying view requests`(): Unit = runBlocking {
        `when`(
            viewRequestService.denyViewRequest(
                viewRequest.id,
                viewRequest.requester
            )
        ).thenReturn(viewRequest.copy(status = ViewRequestStatus.DENIED))

        webClient.post()
            .uri("/api/v2/viewRequests/deny/${viewRequest.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(ViewRequestDto::class.java)

        verify(viewRequestService).denyViewRequest(viewRequest.id, viewRequest.requester)
    }
}