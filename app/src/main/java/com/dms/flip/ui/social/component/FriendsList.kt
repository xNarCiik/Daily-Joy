package com.dms.flip.ui.social.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.dms.flip.ui.social.Friend
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewFriends
import kotlinx.coroutines.delay

@Composable
fun FriendsList(
    friends: List<Friend>,
    onFriendClick: (Friend) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            items = friends,
            key = { _, friend -> friend.id }
        ) { index, friend ->
            AnimatedFriendItem(
                friend = friend,
                onClick = { onFriendClick(friend) },
                animationDelay = index * 50
            )
        }
    }
}

@Composable
private fun AnimatedFriendItem(
    friend: Friend,
    onClick: () -> Unit,
    animationDelay: Int = 0
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "friendItemAlpha"
    )

    FriendItem(
        friend = friend,
        onClick = onClick,
        modifier = Modifier.alpha(alpha)
    )
}

@LightDarkPreview
@Composable
private fun FriendsListPreview() {
    FlipTheme {
        Surface {
            FriendsList(
                friends = previewFriends,
                onFriendClick = {}
            )
        }
    }
}
