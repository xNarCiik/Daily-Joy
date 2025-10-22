package com.dms.dailyjoy.ui.social

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.settings.manage.component.ErrorState
import com.dms.dailyjoy.ui.settings.manage.component.LoadingState
import com.dms.dailyjoy.ui.social.component.AddFriendSection
import com.dms.dailyjoy.ui.social.component.EmptyFriendsState
import com.dms.dailyjoy.ui.social.component.FriendCard
import com.dms.dailyjoy.ui.social.component.FriendOptionsDialog
import com.dms.dailyjoy.ui.social.component.FriendsStatsHeader
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewFriends

@Composable
fun SocialScreen(
    modifier: Modifier = Modifier,
    uiState: SocialUiState,
    onEvent: (SocialEvent) -> Unit
) {
    var showFriendOptionsDialog by remember { mutableStateOf<Friend?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                LoadingState()
            }
            uiState.error != null -> {
                ErrorState(
                    message = uiState.error,
                    onRetry = { onEvent(SocialEvent.OnRetryClicked) }
                )
            }
            else -> {
                FriendsContent(
                    friends = uiState.friends,
                    newFriendUsername = uiState.newFriendUsername,
                    isAddingFriend = uiState.isAddingFriend,
                    addFriendError = uiState.addFriendError,
                    onFriendClick = { showFriendOptionsDialog = it },
                    onEvent = onEvent
                )
            }
        }

        // Friend Options Dialog
        showFriendOptionsDialog?.let { friend ->
            FriendOptionsDialog(
                friend = friend,
                onDismiss = { showFriendOptionsDialog = null },
                onViewStats = {
                    onEvent(SocialEvent.OnViewFriendStats(friend))
                    showFriendOptionsDialog = null
                },
                onRemoveFriend = {
                    onEvent(SocialEvent.OnRemoveFriend(friend))
                    showFriendOptionsDialog = null
                }
            )
        }
    }
}

@Composable
private fun FriendsContent(
    friends: List<Friend>,
    newFriendUsername: String,
    isAddingFriend: Boolean,
    addFriendError: String?,
    onFriendClick: (Friend) -> Unit,
    onEvent: (SocialEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Header Stats
        FriendsStatsHeader(friendsCount = friends.size)

        Spacer(modifier = Modifier.height(16.dp))

        // Add Friend Section
        AddFriendSection(
            username = newFriendUsername,
            isLoading = isAddingFriend,
            error = addFriendError,
            onUsernameChange = { onEvent(SocialEvent.OnUsernameChanged(it)) },
            onAddFriend = { onEvent(SocialEvent.OnAddFriendClicked) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Friends List
        if (friends.isEmpty()) {
            EmptyFriendsState(modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = friends,
                    key = { it.id }
                ) { friend ->
                    FriendCard(
                        friend = friend,
                        onClick = { onFriendClick(friend) }
                    )
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun SocialScreenPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SocialScreen(
                uiState = SocialUiState(friends = previewFriends),
                onEvent = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun SocialScreenEmptyPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SocialScreen(
                uiState = SocialUiState(friends = emptyList()),
                onEvent = {}
            )
        }
    }
}
