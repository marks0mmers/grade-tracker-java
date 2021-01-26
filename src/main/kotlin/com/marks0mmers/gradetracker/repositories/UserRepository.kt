package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.models.persistent.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsername(username: String): Mono<User>
}