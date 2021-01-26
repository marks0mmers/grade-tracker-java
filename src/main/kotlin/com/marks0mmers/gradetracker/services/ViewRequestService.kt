package com.marks0mmers.gradetracker.services

import com.marks0mmers.gradetracker.models.constants.ViewRequestStatus
import com.marks0mmers.gradetracker.models.dto.ViewRequestDto
import com.marks0mmers.gradetracker.models.persistent.ViewRequest
import com.marks0mmers.gradetracker.repositories.ViewRequestRepository
import com.marks0mmers.gradetracker.util.panic
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ViewRequestService {

    @Autowired
    private lateinit var viewRequestRepository: ViewRequestRepository
    @Autowired
    private lateinit var userService: UserService

    suspend fun getAllForRequester(requesterUsername: String): Flow<ViewRequestDto> {
        val user = userService.findByUsername(requesterUsername)
        return viewRequestRepository.findByRequester(user.id!!).asFlow()
            .map { ViewRequestDto(it) }
    }

    suspend fun getAllForReceiver(receiverUsername: String): Flow<ViewRequestDto> {
        val user = userService.findByUsername(receiverUsername)
        return viewRequestRepository.findByReceiver(user.id!!).asFlow()
            .map { ViewRequestDto(it) }
    }

    suspend fun checkIfRequestExists(requesterId: String, receiverId: String): ViewRequestDto {
        val allForRequester = getAllForRequester(requesterId)
        val req = allForRequester.filter { vr -> vr.receiver == receiverId }
        req.onEmpty { panic("View Request with Requester ID: $requesterId & Receiver ID: $receiverId doesn't exist") }
        return req.first()
    }

    suspend fun sendUserViewRequest(currentUsername: String, userToRequest: String): ViewRequestDto {
        val currentUser = userService.findByUsername(currentUsername)
        val newRequest = ViewRequest(
            null,
            ViewRequestStatus.SENT.ordinal,
            currentUser.id!!,
            userToRequest
        )

        val viewRequest = viewRequestRepository
            .save(newRequest)
            .awaitFirstOrElse { panic("Failed to save View Request1") }
        return ViewRequestDto(viewRequest)
    }

    suspend fun approveViewRequest(requestId: String, currentUsername: String): ViewRequestDto {
        val currentUser = userService.findByUsername(currentUsername)
        val requestToApprove = viewRequestRepository
            .findById(requestId)
            .awaitFirstOrElse { panic("Cannot find View Request with ID: $requestId") }

        if (requestToApprove.receiver != currentUser.id) {
            panic("Cannot approve a request that is not yours")
        }

        requestToApprove.status = ViewRequestStatus.APPROVED.ordinal

        val viewRequest = viewRequestRepository
            .save(requestToApprove)
            .awaitFirstOrElse { panic("Failed to save View Request1") }
        return ViewRequestDto(viewRequest)
    }

    suspend fun denyViewRequest(requestId: String, currentUsername: String): ViewRequestDto {
        val currentUser = userService.findByUsername(currentUsername)
        val requestToApprove = viewRequestRepository
            .findById(requestId)
            .awaitFirstOrElse { panic("Cannot find View Request with ID: $requestId") }

        if (requestToApprove.receiver != currentUser.id) {
            panic("Cannot approve a request that is not yours")
        }

        requestToApprove.status = ViewRequestStatus.DENIED.ordinal

        val viewRequest = viewRequestRepository
            .save(requestToApprove)
            .awaitFirstOrElse { panic("Failed to save View Request1") }
        return ViewRequestDto(viewRequest)
    }
}