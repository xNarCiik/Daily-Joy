
package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.ui.social.Friend
import kotlinx.coroutines.flow.Flow

interface SocialRepository {
    fun getFriends(): Flow<List<Friend>>
    suspend fun addFriend(username: String)
    suspend fun removeFriend(friend: Friend)
}
