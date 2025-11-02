package com.dms.flip.domain.repository.community

import com.dms.flip.domain.model.community.FriendRequest
import kotlinx.coroutines.flow.Flow

interface RequestsRepository {
    fun observePendingReceived(): Flow<List<FriendRequest>>
    fun observePendingSent(): Flow<List<FriendRequest>>
    suspend fun accept(requestId: String)
    suspend fun decline(requestId: String)
    suspend fun cancelSent(requestId: String)
    suspend fun send(toUserId: String): FriendRequest
}
