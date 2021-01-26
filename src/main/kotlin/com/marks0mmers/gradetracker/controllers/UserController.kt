package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.models.vm.AuthRequestVM
import com.marks0mmers.gradetracker.models.vm.CreateUserVM
import com.marks0mmers.gradetracker.models.dto.UserDto
import com.marks0mmers.gradetracker.models.persistent.User
import com.marks0mmers.gradetracker.services.UserService
import com.marks0mmers.gradetracker.util.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.json

@Configuration
class UserController {

    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var jwtUtil: JWTUtil

    @Bean
    fun userRouter() = coRouter {
        "/api/v2".nest {
            POST("/users/login") { req ->
                val ar = req.awaitBody<AuthRequestVM>()
                val user = userService.login(ar.username, ar.password)
                ok().json()
                    .bodyValueAndAwait(addToken(user))
            }

            POST("/users") { req ->
                val user = req.awaitBody<CreateUserVM>()
                val createdUser = userService.createUser(user)
                ok().json()
                    .bodyValueAndAwait(createdUser)
            }

            GET("/users/{id}") { req ->
                val userId = req.pathVariable("id")
                val user = userService.getUserById(userId)
                ok().json()
                    .bodyValueAndAwait(user)
            }
        }
    }

    private fun addToken(user: User): UserDto {
        val userDto = UserDto(user)
        userDto.token = jwtUtil.generateToken(user)
        return userDto
    }
}
