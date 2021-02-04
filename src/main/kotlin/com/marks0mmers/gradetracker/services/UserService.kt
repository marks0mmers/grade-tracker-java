package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.config.PasswordEncoder
import com.marks0mmers.gradetracker.models.constants.Role
import com.marks0mmers.gradetracker.models.dto.UserDto
import com.marks0mmers.gradetracker.models.persistent.User
import com.marks0mmers.gradetracker.models.vm.CreateUserVM
import com.marks0mmers.gradetracker.repositories.UserRepository
import com.marks0mmers.gradetracker.util.panic
import com.marks0mmers.gradetracker.util.unauthorizedPanic
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var passwordEncoder: PasswordEncoder

    suspend fun login(username: String, password: String): User {
        val user = userRepository
            .findByUsername(username)
            .awaitFirstOrElse { panic("Cannot find user with username: $username") }
        return if (passwordEncoder.matches(password, user.password)) user else unauthorizedPanic("Cannot Login")
    }

    suspend fun findByUsername(username: String): UserDto {
        return userRepository
            .findByUsername(username)
            .awaitFirstOrElse { panic("Cannot find user with username: $username") }
            .let { UserDto(it) }
    }

    suspend fun createUser(user: CreateUserVM): UserDto {
        return userRepository
            .insert(
                User(
                    username = user.username,
                    password = passwordEncoder.encode(user.password),
                    firstName = user.firstName,
                    lastName = user.lastName,
                    enabled = true,
                    roles = listOf(Role.ROLE_USER)
                )
            )
            .awaitFirst()
            .let { UserDto(it) }
    }

    suspend fun getUserById(userId: String): UserDto {
        return userRepository
            .findById(userId)
            .awaitFirstOrElse { panic("Cannot find user with id: $userId") }
            .let { UserDto(it) }
    }
}
