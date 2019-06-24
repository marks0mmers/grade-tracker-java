package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.persistent.Grade
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface GradeRepository : ReactiveMongoRepository<Grade, String>