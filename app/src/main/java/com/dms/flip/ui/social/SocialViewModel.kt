package com.dms.flip.ui.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.R
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.domain.usecase.social.AddFriendUseCase
import com.dms.flip.domain.usecase.social.GetFriendsUseCase
import com.dms.flip.domain.usecase.social.RemoveFriendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val addFriendUseCase: AddFriendUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase
    // TODO: Add use cases for invitations, requests, and search
    // private val getPleasureInvitationsUseCase: GetPleasureInvitationsUseCase,
    // private val acceptInvitationUseCase: AcceptInvitationUseCase,
    // private val declineInvitationUseCase: DeclineInvitationUseCase,
    // private val getPendingRequestsUseCase: GetPendingRequestsUseCase,
    // private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    // private val declineFriendRequestUseCase: DeclineFriendRequestUseCase,
    // private val searchUsersUseCase: SearchUsersUseCase,
) : ViewModel() {

    // TODO Remove mock
    private val _uiState = MutableStateFlow(
        SocialUiState(
            pleasureInvitations = listOf(
                PleasureInvitation(
                    id = "1",
                    friendId = "1",
                    friendName = "Léa Martin",
                    friendAvatarUrl = null,
                    pleasureCategory = PleasureCategory.SOCIAL,
                    pleasureTitle = "Un café au soleil ce matin",
                    pleasureEmoji = "☕"
                )
            ),
            pendingRequests = listOf(
                FriendRequest(
                    id = "2",
                    userId = "1",
                    username = "Léa Martin",
                    requestedAt = 0L,
                    avatarUrl = ""
                )
            ),
            sentRequests = listOf(
                FriendRequest(
                    id = "3",
                    userId = "1",
                    username = "Théo Martin",
                    requestedAt = 0L,
                    avatarUrl = ""
                )
            )
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadFriends()
        // TODO: Load other data
        // loadPleasureInvitations()
        // loadPendingRequests()
    }

    private fun loadFriends() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            getFriendsUseCase().collectLatest { friends ->
                _uiState.update {
                    it.copy(
                        friends = friends,
                        isLoading = false,
                        error = null
                    )
                }
            }
        } catch (_: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = R.string.social_error_load_friends
                )
            }
        }
    }

    // TODO: Implement when use case is ready
    /*
    private fun loadPleasureInvitations() = viewModelScope.launch {
        try {
            getPleasureInvitationsUseCase().collectLatest { invitations ->
                _uiState.update { it.copy(pleasureInvitations = invitations) }
            }
        } catch (_: Exception) {
            // Handle error silently or show notification
        }
    }
    */

    // TODO: Implement when use case is ready
    /*
    private fun loadPendingRequests() = viewModelScope.launch {
        try {
            getPendingRequestsUseCase().collectLatest { requests ->
                _uiState.update { it.copy(pendingRequests = requests) }
            }
        } catch (_: Exception) {
            // Handle error silently or show notification
        }
    }
    */

    fun onEvent(event: SocialEvent) {
        when (event) {
            // Search events
            is SocialEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                if (event.query.isNotBlank()) {
                    searchUsers(event.query)
                } else {
                    _uiState.update { it.copy(searchResults = emptyList()) }
                }
            }

            SocialEvent.OnSearchClicked -> {
                // TODO: Handle search click (expand search, navigate to search screen, etc.)
            }

            // Friend actions
            is SocialEvent.OnAddFriend -> addFriend(event.userId)
            is SocialEvent.OnRemoveFriend -> removeFriend(event.friend)
            is SocialEvent.OnViewFriendStats -> {
                // TODO: Handle navigation to friend stats
            }

            // Invitation events
            is SocialEvent.OnAcceptInvitation -> acceptInvitation(event.invitation)
            is SocialEvent.OnDeclineInvitation -> declineInvitation(event.invitation)

            // Friend request events
            SocialEvent.OnPendingRequestsClicked -> {
                // TODO: Handle navigation to pending requests screen
            }

            is SocialEvent.OnAcceptFriendRequest -> acceptFriendRequest(event.request)
            is SocialEvent.OnDeclineFriendRequest -> declineFriendRequest(event.request)

            // Error handling
            SocialEvent.OnRetryClicked -> {
                loadFriends()
                // TODO: Also retry other loads
                // loadPleasureInvitations()
                // loadPendingRequests()
            }
        }
    }

    private fun searchUsers(query: String) = viewModelScope.launch {
        if (query.length < 2) return@launch // Minimum 2 characters

        _uiState.update { it.copy(isSearching = true) }

        // TODO: Implement when use case is ready
        /*
        try {
            val results = searchUsersUseCase(query)
            _uiState.update {
                it.copy(
                    searchResults = results,
                    isSearching = false
                )
            }
        } catch (_: Exception) {
            _uiState.update {
                it.copy(
                    isSearching = false,
                    error = R.string.social_error_search_failed
                )
            }
        }
        */

        // Temporary: Clear searching state
        _uiState.update { it.copy(isSearching = false) }
    }

    private fun addFriend(userId: String) = viewModelScope.launch {
        if (userId.isBlank()) return@launch

        _uiState.update { it.copy(isSearching = true) }
        try {
            addFriendUseCase(userId)
            _uiState.update {
                it.copy(
                    searchQuery = "",
                    searchResults = emptyList(),
                    isSearching = false
                )
            }
            // TODO: Show success message
        } catch (_: Exception) {
            _uiState.update {
                it.copy(
                    isSearching = false,
                    error = R.string.social_error_add_friend
                )
            }
        }
    }

    private fun removeFriend(friend: Friend) = viewModelScope.launch {
        try {
            removeFriendUseCase(friend)
            // TODO: Show success message
        } catch (_: Exception) {
            _uiState.update { it.copy(error = R.string.social_error_remove_friend) }
        }
    }

    private fun acceptInvitation(invitation: PleasureInvitation) = viewModelScope.launch {
        // TODO: Implement when use case is ready
        /*
        try {
            acceptInvitationUseCase(invitation)
            // Remove from local state
            _uiState.update {
                it.copy(
                    pleasureInvitations = it.pleasureInvitations.filter { inv ->
                        inv.id != invitation.id
                    }
                )
            }
            // TODO: Show success message
        } catch (_: Exception) {
            _uiState.update { it.copy(error = R.string.social_error_accept_invitation) }
        }
        */
    }

    private fun declineInvitation(invitation: PleasureInvitation) = viewModelScope.launch {
        // TODO: Implement when use case is ready
        /*
        try {
            declineInvitationUseCase(invitation)
            // Remove from local state
            _uiState.update {
                it.copy(
                    pleasureInvitations = it.pleasureInvitations.filter { inv ->
                        inv.id != invitation.id
                    }
                )
            }
        } catch (_: Exception) {
            _uiState.update { it.copy(error = R.string.social_error_decline_invitation) }
        }
        */
    }

    private fun acceptFriendRequest(request: FriendRequest) = viewModelScope.launch {
        // TODO: Implement when use case is ready
        /*
        try {
            acceptFriendRequestUseCase(request)
            // Remove from local state
            _uiState.update {
                it.copy(
                    pendingRequests = it.pendingRequests.filter { req ->
                        req.id != request.id
                    }
                )
            }
            // TODO: Show success message
        } catch (_: Exception) {
            _uiState.update { it.copy(error = R.string.social_error_accept_request) }
        }
        */
    }

    private fun declineFriendRequest(request: FriendRequest) = viewModelScope.launch {
        // TODO: Implement when use case is ready
        /*
        try {
            declineFriendRequestUseCase(request)
            // Remove from local state
            _uiState.update {
                it.copy(
                    pendingRequests = it.pendingRequests.filter { req ->
                        req.id != request.id
                    }
                )
            }
        } catch (_: Exception) {
            _uiState.update { it.copy(error = R.string.social_error_decline_request) }
        }
        */
    }
}
