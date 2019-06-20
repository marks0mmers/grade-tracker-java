package com.marks0mmers.gradetracker.repositories

import com.marks0mmers.gradetracker.persistent.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface UserRepository : ReactiveMongoRepository<User, String>