package com.dms.dailyjoy.ui.social

import androidx.annotation.StringRes
import com.dms.dailyjoy.data.model.PleasureCategory


data class SocialUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val newFriendUsername: String = "",
    val isAddingFriend: Boolean = false,
    @StringRes val addFriendError: Int? = null,
    @StringRes val error: Int? = null
)

sealed interface SocialEvent {
    data class OnUsernameChanged(val username: String) : SocialEvent
    data object OnAddFriendClicked : SocialEvent
    data class OnRemoveFriend(val friend: Friend) : SocialEvent
    data class OnViewFriendStats(val friend: Friend) : SocialEvent
    data object OnRetryClicked : SocialEvent
}


data class Friend(
    val id: String,
    val username: String,
    val streak: Int,
    val currentPleasure: FriendPleasure? = null,
    val avatarUrl: String? = null
)

data class FriendPleasure(
    val title: String,
    val category: PleasureCategory,
    val status: PleasureStatus
)

enum class PleasureStatus {
    IN_PROGRESS,
    COMPLETED
}
