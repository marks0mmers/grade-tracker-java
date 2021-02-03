package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.models.persistent.CourseAverageTracking
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface CourseAverageTrackingRepository : ReactiveMongoRepository<CourseAverageTracking, String> {
    fun findByCourseId(courseId: String): Flux<CourseAverageTracking>
}
