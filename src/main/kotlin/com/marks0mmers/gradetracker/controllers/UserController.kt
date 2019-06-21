package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.dto.AuthRequest
import com.marks0mmers.gradetracker.dto.CreateUserDto
import com.marks0mmers.gradetracker.dto.UserDto
import com.marks0mmers.gradetracker.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
        val userService: UserService
) {
    @PostMapping("/login")
    fun login(@RequestBody ar: AuthRequest) = userService
            .login(ar.username, ar.password)
            .map { ResponseEntity.ok( UserDto(it)) }
            .defaultIfEmpty( ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() )


    @PostMapping
    fun createUser(@RequestBody user: CreateUserDto) = userService
            .createUser(user)
            .map { UserDto(it) }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    fun getUserById(@PathVariable("id") userId: String) = userService
            .getUserById(userId)
            .map { UserDto(it) }
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}
