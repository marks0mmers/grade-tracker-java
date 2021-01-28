package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.models.dto.GradeDto
import com.marks0mmers.gradetracker.models.persistent.Grade
import com.marks0mmers.gradetracker.models.vm.GradeSubmissionVM
import com.marks0mmers.gradetracker.repositories.GradeRepository
import com.marks0mmers.gradetracker.util.panic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GradeService {

    @Autowired
    private lateinit var gradeRepository: GradeRepository

    fun getGradesFromCategory(gradeCategoryId: String): Flow<GradeDto> {
        return gradeRepository.findByGradeCategoryId(gradeCategoryId).asFlow()
            .map { GradeDto(it) }
    }

    suspend fun getGrade(gradeId: String): GradeDto {
        val grade = gradeRepository.findById(gradeId).awaitFirstOrElse { panic("Cannot find grade by ID: $gradeId") }
        return GradeDto(grade)
    }

    suspend fun newGrade(gradeCategoryId: String, gradeSubmission: GradeSubmissionVM): GradeDto {
        val createdGrade = gradeRepository.save(Grade(gradeSubmission, gradeCategoryId)).awaitFirstOrElse { panic("Unable to save new grade") }
        return GradeDto(createdGrade)
    }

    suspend fun updateGrade(gradeId: String, grade: GradeDto): GradeDto {
        val gradeToUpdate = getGrade(gradeId)
        val updatedGrade = gradeRepository
            .save(Grade(gradeToUpdate.copy(
                name = grade.name,
                grade = grade.grade
            )))
            .awaitFirstOrElse { panic("Unable to update grade") }
        return GradeDto(updatedGrade)
    }

    suspend fun deleteGrade(gradeId: String): GradeDto {
        val grade = gradeRepository.findById(gradeId).awaitFirstOrElse { panic("Cannot find grade by ID: $gradeId") }
        gradeRepository.deleteById(gradeId)
        return GradeDto(grade)
    }
}