
package com.dms.flip.domain.repository

import com.dms.flip.domain.model.community.Friend
import kotlinx.coroutines.flow.Flow

interface SocialRepository {
    fun getFriends(): Flow<List<Friend>>
    suspend fun addFriend(username: String)
    suspend fun removeFriend(friend: Friend)
}
