package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.RequestDto
import kotlinx.coroutines.flow.Flow

interface RequestsSource {
    fun observePendingReceived(uid: String): Flow<List<Pair<String, RequestDto>>>
    fun observePendingSent(uid: String): Flow<List<Pair<String, RequestDto>>>
    suspend fun accept(uid: String, requestId: String)
    suspend fun decline(uid: String, requestId: String)
    suspend fun cancelSent(uid: String, requestId: String)
    suspend fun send(uid: String, toUserId: String): Pair<String, RequestDto>
    suspend fun getPendingReceivedIds(uid: String): Set<String>
    suspend fun getPendingSentIds(uid: String): Set<String>
}
