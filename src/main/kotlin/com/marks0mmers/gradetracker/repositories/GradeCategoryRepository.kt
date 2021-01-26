package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.models.persistent.GradeCategory
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface GradeCategoryRepository : ReactiveMongoRepository<GradeCategory, String>