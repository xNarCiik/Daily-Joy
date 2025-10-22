package com.dms.dailyjoy.ui.social

data class SocialUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val newFriendUsername: String = "",
    val isAddingFriend: Boolean = false,
    val addFriendError: String? = null,
    val error: String? = null
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
    val currentPleasure: FriendPleasure? = null
)

data class FriendPleasure(
    val title: String,
    val status: PleasureStatus
)

enum class PleasureStatus {
    IN_PROGRESS,
    COMPLETED
}
