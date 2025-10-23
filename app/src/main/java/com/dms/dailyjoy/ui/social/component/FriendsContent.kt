package com.dms.dailyjoy.ui.social.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.component.AppHeader
import com.dms.dailyjoy.ui.social.Friend
import com.dms.dailyjoy.ui.social.SocialEvent

@Composable
fun FriendsContent(
    friends: List<Friend>,
    newFriendUsername: String,
    isAddingFriend: Boolean,
    addFriendError: String?,
    onFriendClick: (Friend) -> Unit,
    onEvent: (SocialEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        AppHeader(
            title = stringResource(R.string.social_title),
            subtitle = stringResource(R.string.social_header_subtitle),
            icon = Icons.Default.Groups,
            value = friends.size,
            valueIcon = Icons.Default.Groups
        )

        AddFriendSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            username = newFriendUsername,
            isLoading = isAddingFriend,
            error = addFriendError,
            onUsernameChange = { onEvent(SocialEvent.OnUsernameChanged(it)) },
            onAddFriend = { onEvent(SocialEvent.OnAddFriendClicked) }
        )

        if (friends.isEmpty()) {
            EmptyFriendsState(modifier = Modifier.weight(1f))
        } else {
            FriendsList(
                friends = friends,
                onFriendClick = onFriendClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
