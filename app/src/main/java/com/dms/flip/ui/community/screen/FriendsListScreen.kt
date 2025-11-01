package com.dms.flip.ui.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import com.dms.flip.ui.community.CommunityEvent
import com.dms.flip.ui.community.CommunityUiState
import com.dms.flip.ui.community.Friend
import com.dms.flip.ui.community.component.FriendListItem
import com.dms.flip.ui.community.component.FriendOptionsDialog
import com.dms.flip.ui.community.component.FriendsListTopBar
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewCommunityUiStateFull

@Composable
fun FriendsListScreen(
    modifier: Modifier = Modifier,
    uiState: CommunityUiState,
    onEvent: (CommunityEvent) -> Unit
) {
    var selectedFriend by remember { mutableStateOf<Friend?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        FriendsListTopBar(
            onSearchClick = { onEvent(CommunityEvent.OnSearchClicked) },
            onAddFriendClick = { onEvent(CommunityEvent.OnAddFriendClicked) }
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = uiState.friends, key = { it.id }) { friend ->
                FriendListItem(
                    friend = friend,
                    onClick = { onEvent(CommunityEvent.OnFriendClicked(friend)) },
                    onMenuClick = { selectedFriend = friend },
                    onQuickAction = { onEvent(CommunityEvent.OnInviteFriendToPleasure(friend)) }
                )
            }
        }
    }

    selectedFriend?.let { friend ->
        FriendOptionsDialog(
            friend = friend,
            onDismiss = { selectedFriend = null },
            onViewProfile = {
                onEvent(CommunityEvent.OnFriendClicked(friend))
                selectedFriend = null
            },
            onInvite = {
                onEvent(CommunityEvent.OnInviteFriendToPleasure(friend))
                selectedFriend = null
            },
            onRemove = {
                onEvent(CommunityEvent.OnRemoveFriend(friend))
                selectedFriend = null
            }
        )
    }
}

@LightDarkPreview
@Composable
private fun FriendsListScreenPreview() {
    FlipTheme {
        FriendsListScreen(
            uiState = previewCommunityUiStateFull,
            onEvent = {}
        )
    }
}
