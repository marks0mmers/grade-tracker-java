package com.marks0mmers.gradetracker.controllers

import com.marks0mmers.gradetracker.config.PBKDF2Encoder
import com.marks0mmers.gradetracker.dto.AuthRequest
import com.marks0mmers.gradetracker.dto.AuthResponse
import com.marks0mmers.gradetracker.dto.CreateUserDto
import com.marks0mmers.gradetracker.services.UserService
import com.marks0mmers.gradetracker.util.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
        val jwtUtil: JWTUtil,
        val passwordEncoder: PBKDF2Encoder,
        val userService: UserService
) {
    @PostMapping("/login")
    fun login(@RequestBody ar: AuthRequest) = userService
            .findByUsername(ar.username)
            .map {
                if (it != null && passwordEncoder.matches(ar.password, it.password)) {
                    ResponseEntity.ok(AuthResponse(jwtUtil.generateToken(it)))
                } else {
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<Void>()
                }
            }
            .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())

    @PostMapping
    fun createUser(@RequestBody user: CreateUserDto) = userService
            .createUser(user)

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    fun getUserById(@PathVariable("id") userId: String) = userService
            .getUserById(userId)
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty(ResponseEntity.notFound().build())
}
