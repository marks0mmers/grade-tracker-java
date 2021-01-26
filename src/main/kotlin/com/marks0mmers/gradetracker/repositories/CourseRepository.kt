package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.models.persistent.Course
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CourseRepository : ReactiveMongoRepository<Course, String>