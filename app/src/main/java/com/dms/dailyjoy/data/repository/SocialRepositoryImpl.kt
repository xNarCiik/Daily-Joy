package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.domain.repository.SocialRepository
import com.dms.dailyjoy.ui.social.Friend
import com.dms.dailyjoy.ui.util.previewFriends
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SocialRepositoryImpl @Inject constructor() : SocialRepository {

    private val friends = MutableStateFlow(previewFriends)

    override fun getFriends(): Flow<List<Friend>> = friends

    override suspend fun addFriend(username: String) {
        delay(1000) // Simulate network delay
        if (username == "error") {
            throw Exception("Impossible d'ajouter cet ami")
        }
        val newFriend = Friend(System.currentTimeMillis().toString(), username, 0)
        friends.update { it + newFriend }
    }

    override suspend fun removeFriend(friend: Friend) {
        delay(500) // Simulate network delay
        friends.update { it.filter { it.id != friend.id } }
    }
}
