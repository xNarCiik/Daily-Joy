package com.dms.flip.ui.community

import androidx.annotation.StringRes
import com.dms.flip.data.model.PleasureCategory

data class CommunityUiState(
    val isLoading: Boolean = false,
    val selectedTab: CommunityTab = CommunityTab.FRIENDS,
    
    // Friends Feed
    val friendsPosts: List<FriendPost> = emptyList(),
    
    // Friends List
    val friends: List<Friend> = emptyList(),
    
    // Suggestions
    val suggestions: List<FriendSuggestion> = emptyList(),
    val suggestionFilter: SuggestionFilter = SuggestionFilter.ALL,
    
    // Friend Requests
    val pendingRequests: List<FriendRequest> = emptyList(),
    val sentRequests: List<FriendRequest> = emptyList(),
    
    // Search
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val searchResults: List<UserSearchResult> = emptyList(),
    
    @StringRes val error: Int? = null
)

enum class CommunityTab {
    FRIENDS,
    SUGGESTIONS,
    INVITATIONS
}

enum class SuggestionFilter {
    ALL,
    COMMUNITIES,
    CONTACTS
}

// ==================== MODELS ====================

/**
 * Post d'un ami dans le feed
 */
data class FriendPost(
    val id: String,
    val friend: Friend,
    val content: String,
    val timestamp: Long,
    val likesCount: Int,
    val commentsCount: Int,
    val isLiked: Boolean = false,
    val pleasureCategory: PleasureCategory? = null
)

/**
 * Ami avec streak et plaisir actuel
 */
data class Friend(
    val id: String,
    val username: String,
    val handle: String,
    val avatarUrl: String? = null,
    val streak: Int = 0,
    val isOnline: Boolean = false,
    val currentPleasure: FriendPleasure? = null,
    val favoriteCategory: PleasureCategory? = null
)

/**
 * Plaisir actuel d'un ami
 */
data class FriendPleasure(
    val title: String,
    val category: PleasureCategory,
    val status: PleasureStatus
)

enum class PleasureStatus {
    IN_PROGRESS,
    COMPLETED
}

/**
 * Suggestion d'ami
 */
data class FriendSuggestion(
    val id: String,
    val username: String,
    val handle: String,
    val avatarUrl: String? = null,
    val mutualFriendsCount: Int = 0,
    val source: SuggestionSource = SuggestionSource.ALGORITHM
)

enum class SuggestionSource {
    ALGORITHM,
    COMMUNITY,
    CONTACTS
}

/**
 * Demande d'ami
 */
data class FriendRequest(
    val id: String,
    val userId: String,
    val username: String,
    val handle: String,
    val avatarUrl: String? = null,
    val requestedAt: Long,
    val source: FriendRequestSource = FriendRequestSource.SEARCH
)

enum class FriendRequestSource {
    SEARCH,
    SUGGESTION
}

/**
 * Résultat de recherche
 */
data class UserSearchResult(
    val id: String,
    val username: String,
    val handle: String,
    val avatarUrl: String? = null,
    val relationshipStatus: RelationshipStatus = RelationshipStatus.NONE
)

enum class RelationshipStatus {
    NONE,
    FRIEND,
    PENDING_SENT,
    PENDING_RECEIVED
}

/**
 * Profil public d'un utilisateur
 */
data class PublicProfile(
    val id: String,
    val username: String,
    val handle: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val friendsCount: Int = 0,
    val daysCompleted: Int = 0,
    val currentStreak: Int = 0,
    val recentActivities: List<RecentActivity> = emptyList(),
    val relationshipStatus: RelationshipStatus = RelationshipStatus.NONE
)

/**
 * Activité récente d'un utilisateur
 */
data class RecentActivity(
    val id: String,
    val pleasureTitle: String,
    val category: PleasureCategory,
    val completedAt: Long,
    val isCompleted: Boolean
)

// ==================== EVENTS ====================

sealed interface CommunityEvent {
    // Navigation
    data class OnTabSelected(val tab: CommunityTab) : CommunityEvent
    data object OnSearchClicked : CommunityEvent
    data object OnAddFriendClicked : CommunityEvent
    data object OnCreatePostClicked : CommunityEvent
    
    // Friends Feed
    data class OnPostLiked(val postId: String) : CommunityEvent
    data class OnPostCommentClicked(val post: FriendPost) : CommunityEvent
    data class OnPostMenuClicked(val post: FriendPost) : CommunityEvent
    
    // Friends List
    data class OnFriendClicked(val friend: Friend) : CommunityEvent
    data class OnFriendMenuClicked(val friend: Friend) : CommunityEvent
    data class OnInviteFriendToPleasure(val friend: Friend) : CommunityEvent
    data class OnRemoveFriend(val friend: Friend) : CommunityEvent
    
    // Suggestions
    data class OnSuggestionFilterChanged(val filter: SuggestionFilter) : CommunityEvent
    data class OnAddSuggestion(val suggestion: FriendSuggestion) : CommunityEvent
    data class OnHideSuggestion(val suggestion: FriendSuggestion) : CommunityEvent
    
    // Friend Requests
    data object OnFriendRequestsClicked : CommunityEvent
    data class OnAcceptFriendRequest(val request: FriendRequest) : CommunityEvent
    data class OnDeclineFriendRequest(val request: FriendRequest) : CommunityEvent
    data class OnCancelSentRequest(val request: FriendRequest) : CommunityEvent
    
    // Search
    data class OnSearchQueryChanged(val query: String) : CommunityEvent
    data class OnSearchResultClicked(val result: UserSearchResult) : CommunityEvent
    data class OnAddUserFromSearch(val userId: String) : CommunityEvent
    
    // Profile
    data class OnViewProfile(val userId: String) : CommunityEvent
    
    // Error
    data object OnRetryClicked : CommunityEvent
}
