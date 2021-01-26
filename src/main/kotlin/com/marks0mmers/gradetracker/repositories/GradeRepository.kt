package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.models.persistent.Grade
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface GradeRepository : ReactiveMongoRepository<Grade, String> {
    fun findByGradeCategoryId(gradeCategoryId: String): Flux<Grade>
}