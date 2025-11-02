package com.dms.flip.domain.usecase.community

import com.dms.flip.domain.model.community.FriendRequest
import com.dms.flip.domain.repository.community.RequestsRepository
import com.dms.flip.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePendingReceivedUseCase @Inject constructor(
    private val requestsRepository: RequestsRepository
) {
    operator fun invoke(): Flow<List<FriendRequest>> = requestsRepository.observePendingReceived()
}

class ObservePendingSentUseCase @Inject constructor(
    private val requestsRepository: RequestsRepository
) {
    operator fun invoke(): Flow<List<FriendRequest>> = requestsRepository.observePendingSent()
}

class AcceptFriendRequestUseCase @Inject constructor(
    private val requestsRepository: RequestsRepository
) {
    suspend operator fun invoke(requestId: String): Result<Unit> =
        runCatching { requestsRepository.accept(requestId) }
            .fold(
                onSuccess = { Result.Ok(Unit) },
                onFailure = { Result.Err(it) }
            )
}

class DeclineFriendRequestUseCase @Inject constructor(
    private val requestsRepository: RequestsRepository
) {
    suspend operator fun invoke(requestId: String): Result<Unit> =
        runCatching { requestsRepository.decline(requestId) }
            .fold(
                onSuccess = { Result.Ok(Unit) },
                onFailure = { Result.Err(it) }
            )
}

class CancelSentRequestUseCase @Inject constructor(
    private val requestsRepository: RequestsRepository
) {
    suspend operator fun invoke(requestId: String): Result<Unit> =
        runCatching { requestsRepository.cancelSent(requestId) }
            .fold(
                onSuccess = { Result.Ok(Unit) },
                onFailure = { Result.Err(it) }
            )
}

class SendFriendRequestUseCase @Inject constructor(
    private val requestsRepository: RequestsRepository
) {
    suspend operator fun invoke(userId: String): Result<FriendRequest> =
        runCatching { requestsRepository.send(userId) }
            .fold(
                onSuccess = { Result.Ok(it) },
                onFailure = { Result.Err(it) }
            )
}
