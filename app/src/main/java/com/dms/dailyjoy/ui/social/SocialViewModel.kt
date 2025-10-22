
package com.dms.dailyjoy.ui.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.domain.usecase.social.AddFriendUseCase
import com.dms.dailyjoy.domain.usecase.social.GetFriendsUseCase
import com.dms.dailyjoy.domain.usecase.social.RemoveFriendUseCase
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
) : ViewModel() {

    private val _uiState = MutableStateFlow(SocialUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFriends()
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
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }

    fun onEvent(event: SocialEvent) {
        when (event) {
            is SocialEvent.OnUsernameChanged -> {
                _uiState.update { it.copy(newFriendUsername = event.username, addFriendError = null) }
            }
            SocialEvent.OnAddFriendClicked -> addFriend()
            is SocialEvent.OnRemoveFriend -> removeFriend(event.friend)
            is SocialEvent.OnViewFriendStats -> {
                // TODO: Handle navigation to friend stats
            }
            SocialEvent.OnRetryClicked -> loadFriends()
        }
    }

    private fun addFriend() = viewModelScope.launch {
        val username = _uiState.value.newFriendUsername
        if (username.isBlank()) {
            _uiState.update { it.copy(addFriendError = "Le nom d'utilisateur ne peut pas être vide") }
            return@launch
        }

        _uiState.update { it.copy(isAddingFriend = true) }
        try {
            addFriendUseCase(username)
            _uiState.update { it.copy(newFriendUsername = "", isAddingFriend = false) }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isAddingFriend = false,
                    addFriendError = e.message ?: "Impossible d'ajouter cet ami"
                )
            }
        }
    }

    private fun removeFriend(friend: Friend) = viewModelScope.launch {
        try {
            removeFriendUseCase(friend)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message ?: "Erreur lors de la suppression") }
        }
    }
}
