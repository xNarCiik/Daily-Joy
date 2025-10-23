package com.dms.dailyjoy.ui.social

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dms.dailyjoy.ui.component.ErrorState
import com.dms.dailyjoy.ui.component.LoadingState
import com.dms.dailyjoy.ui.social.component.FriendOptionsDialog
import com.dms.dailyjoy.ui.social.component.FriendsContent

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
                    message = stringResource(uiState.error),
                    onRetry = { onEvent(SocialEvent.OnRetryClicked) }
                )
            }

            else -> {
                FriendsContent(
                    friends = uiState.friends,
                    newFriendUsername = uiState.newFriendUsername,
                    isAddingFriend = uiState.isAddingFriend,
                    addFriendError = uiState.addFriendError?.let { stringResource(it) },
                    onFriendClick = { showFriendOptionsDialog = it },
                    onEvent = onEvent
                )
            }
        }

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
