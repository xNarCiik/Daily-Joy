package com.dms.dailyjoy.ui.social.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.social.Friend
import com.dms.dailyjoy.ui.social.SocialEvent

@Composable
fun FriendsList(
    friends: List<Friend>,
    onFriendClick: (Friend) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(
            items = friends,
            key = { _, friend -> friend.id }
        ) { index, friend ->
            FriendFeedItem(
                friend = friend,
                animationDelay = index * 50,
                onClick = { onFriendClick(friend) }
            )
        }
    }
}
