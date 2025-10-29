package com.dms.flip.ui.social

import androidx.annotation.StringRes
import com.dms.flip.data.model.PleasureCategory

data class SocialUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val pleasureInvitations: List<PleasureInvitation> = emptyList(),
    val pendingRequests: List<FriendRequest> = emptyList(),
    val sentRequests: List<FriendRequest> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<UserSearchResult> = emptyList(),
    @StringRes val error: Int? = null
)

data class Friend(
    val id: String,
    val username: String,
    val streak: Int,
    val currentPleasure: FriendPleasure? = null,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false
)

data class FriendPleasure(
    val title: String,
    val category: PleasureCategory,
    val status: PleasureStatus
)

data class PleasureInvitation(
    val id: String,
    val friendId: String,
    val friendName: String,
    val friendAvatarUrl: String?,
    val pleasureTitle: String,
    val pleasureCategory: PleasureCategory
)

data class FriendRequest(
    val id: String,
    val userId: String,
    val username: String,
    val avatarUrl: String?,
    val requestedAt: Long
)

data class UserSearchResult(
    val id: String,
    val username: String,
    val avatarUrl: String?,
    val isFriend: Boolean = false,
    val hasPendingRequest: Boolean = false
)

enum class PleasureStatus {
    IN_PROGRESS,
    COMPLETED
}

sealed interface SocialEvent {
    // Search
    data class OnSearchQueryChanged(val query: String) : SocialEvent
    data object OnSearchClicked : SocialEvent

    // Friend actions
    data class OnAddFriend(val userId: String) : SocialEvent
    data class OnRemoveFriend(val friend: Friend) : SocialEvent
    data class OnViewFriendStats(val friend: Friend) : SocialEvent

    // Invitations
    data class OnAcceptInvitation(val invitation: PleasureInvitation) : SocialEvent
    data class OnDeclineInvitation(val invitation: PleasureInvitation) : SocialEvent

    // Friend requests
    data object OnPendingRequestsClicked : SocialEvent
    data class OnAcceptFriendRequest(val request: FriendRequest) : SocialEvent
    data class OnDeclineFriendRequest(val request: FriendRequest) : SocialEvent

    // Error handling
    data object OnRetryClicked : SocialEvent
}
