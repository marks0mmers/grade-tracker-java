package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.constants.Role
import com.marks0mmers.gradetracker.models.dto.UserDto
import com.marks0mmers.gradetracker.models.persistent.User
import com.marks0mmers.gradetracker.models.vm.AuthRequestVM
import com.marks0mmers.gradetracker.models.vm.CreateUserVM
import com.marks0mmers.gradetracker.services.UserService
import com.marks0mmers.gradetracker.util.JWTUtil
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [UserController::class], properties = ["spring.profiles.active=dev"])
@Import(JWTUtil::class)
@WithMockUser(username = "marks0mmers", roles = ["USER"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTests {
    @MockBean lateinit var userService: UserService
    @Autowired lateinit var webClient: WebTestClient

    private val user = User(
        "marks0mmers",
        "Vv86VRTWmAUvfyaKdaWsMGcYjJAdNH81nOMGY6LBhV4=",
        "Mark",
        "Sommers",
        true,
        listOf(Role.ROLE_USER)
    ).also { it.id = "UserID" }

    private val userDto = UserDto(
        "UserID",
        "marks0mmers",
        "Mark",
        "Sommers",
        true,
        listOf(Role.ROLE_USER)
    )

    @BeforeEach
    fun configure() {
        webClient = webClient.mutateWith(SecurityMockServerConfigurers.csrf())
    }

    @Test
    fun testLogin(): Unit = runBlocking {
        `when`(userService.login(user.username, "Truckin09")).thenReturn(user)

        webClient.post()
            .uri("/api/v2/users/login")
            .accept(APPLICATION_JSON)
            .bodyValue(AuthRequestVM("marks0mmers", "Truckin09"))
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
    }

    @Test
    fun testCreateUser(): Unit = runBlocking {
        val createUserVm = CreateUserVM(
            "marks0mmers",
            "Truckin09",
            "Mark",
            "Sommers"
        )

        `when`(userService.createUser(createUserVm)).thenReturn(userDto)

        webClient.post()
            .uri("/api/v2/users")
            .accept(APPLICATION_JSON)
            .bodyValue(createUserVm)
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)

        verify(userService).createUser(createUserVm)
    }

    @Test
    fun testGetCurrentUser(): Unit = runBlocking {
        `when`(userService.findByUsername(user.username)).thenReturn(userDto)

        webClient.get()
            .uri("/api/v2/users/current")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
    }

    @Test
    fun testGetUserById(): Unit = runBlocking {
        `when`(userService.getUserById(userDto.id)).thenReturn(userDto)

        webClient.get()
            .uri("/api/v2/users/${user.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDto::class.java)
    }
}