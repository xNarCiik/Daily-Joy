package com.dms.flip.data.repository

import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.domain.model.community.Friend
import com.dms.flip.domain.model.community.FriendPleasure
import com.dms.flip.domain.model.community.PleasureStatus
import com.dms.flip.domain.repository.SocialRepository
import com.dms.flip.ui.util.previewFriends
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// TODO FIRESTORE
class SocialRepositoryImpl @Inject constructor() : SocialRepository {

    private val friends = MutableStateFlow(previewFriends)

    override fun getFriends(): Flow<List<Friend>> = friends

    override suspend fun addFriend(username: String) {
        delay(1000) // Simulate network delay
        if (username == "error") {
            throw Exception("Impossible d'ajouter cet ami")
        }
        val newFriend = Friend(
            id = "1",
            username = username,
            handle = "@$username",
            streak = 15,
            isOnline = true,
            favoriteCategory = PleasureCategory.CREATIVE,
            currentPleasure = FriendPleasure(
                title = "Peindre un tableau",
                category = PleasureCategory.ALL,
                status = PleasureStatus.IN_PROGRESS
            )
        )
        friends.update { it + newFriend }
    }

    override suspend fun removeFriend(friend: Friend) {
        delay(500) // Simulate network delay
        friends.update { it.filter { it.id != friend.id } }
    }
}
