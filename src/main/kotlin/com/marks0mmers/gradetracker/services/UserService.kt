package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.config.PBKDF2Encoder
import com.marks0mmers.gradetracker.constants.Role
import com.marks0mmers.gradetracker.dto.CreateUserDto
import com.marks0mmers.gradetracker.persistent.User
import com.marks0mmers.gradetracker.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.security.Principal

@Service
class UserService @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PBKDF2Encoder
) {
    fun currentUser(p: Principal) = findByUsername(p.name)

    fun findByUsername(username: String): Mono<User?> = userRepository
            .findAll()
            .filter { it.username == username }
            .last()

    fun createUser(user: CreateUserDto) = userRepository
            .save(User(
                    username = user.username,
                    password = passwordEncoder.encode(user.password),
                    firstName = user.firstName,
                    lastName = user.lastName,
                    enabled = true,
                    roles = listOf(Role.ROLE_USER)
            ))

    fun getUserById(userId: String) = userRepository
            .findById(userId)
}
