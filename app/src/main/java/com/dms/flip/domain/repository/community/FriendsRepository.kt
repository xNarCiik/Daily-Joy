package com.dms.flip.domain.repository.community

import com.dms.flip.domain.model.community.Friend
import kotlinx.coroutines.flow.Flow

interface FriendsRepository {
    fun observeFriends(): Flow<List<Friend>>
    suspend fun removeFriend(friendId: String)
}
