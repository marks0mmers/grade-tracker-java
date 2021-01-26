package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.models.persistent.ViewRequest
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface ViewRequestRepository : ReactiveMongoRepository<ViewRequest, String> {
    fun findByRequester(requesterId: String): Flux<ViewRequest>
    fun findByReceiver(receiverId: String): Flux<ViewRequest>
}