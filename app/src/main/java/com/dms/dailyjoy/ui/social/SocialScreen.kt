package com.dms.dailyjoy.ui.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.settings.manage.component.ErrorState
import com.dms.dailyjoy.ui.settings.manage.component.LoadingState
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

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

@Composable
private fun FriendsStatsHeader(
    friendsCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.social_header_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.social_header_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "$friendsCount",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun AddFriendSection(
    username: String,
    isLoading: Boolean,
    error: String?,
    onUsernameChange: (String) -> Unit,
    onAddFriend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.social_add_friend_label)) },
                placeholder = { Text(stringResource(R.string.social_username_placeholder)) },
                isError = error != null,
                enabled = !isLoading,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )

            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(enabled = !isLoading && username.isNotBlank()) { onAddFriend() },
                color = if (username.isNotBlank() && !isLoading) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.social_add_friend_button),
                            tint = if (username.isNotBlank()) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun FriendCard(
    friend: Friend,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = friend.username.first().uppercaseChar().toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Friend Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = friend.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Current Pleasure Status
                if (friend.currentPleasure != null) {
                    PleasureStatusCompact(pleasure = friend.currentPleasure)
                } else {
                    Text(
                        text = stringResource(R.string.social_no_pleasure_today),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            // Streak Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${friend.streak}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Text(
                        text = "🔥",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun PleasureStatusCompact(
    pleasure: FriendPleasure,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (pleasure.status) {
                PleasureStatus.COMPLETED -> Icons.Default.Check
                PleasureStatus.IN_PROGRESS -> Icons.Default.HourglassEmpty
            },
            contentDescription = null,
            tint = when (pleasure.status) {
                PleasureStatus.COMPLETED -> Color(0xFF4CAF50)
                PleasureStatus.IN_PROGRESS -> MaterialTheme.colorScheme.tertiary
            },
            modifier = Modifier.size(14.dp)
        )

        Text(
            text = pleasure.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

@Composable
private fun EmptyFriendsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Group,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(20.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.social_empty_state_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.social_empty_state_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FriendOptionsDialog(
    friend: Friend,
    onDismiss: () -> Unit,
    onViewStats: () -> Unit,
    onRemoveFriend: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = friend.username,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                TextButton(
                    onClick = onViewStats,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(stringResource(R.string.social_friend_options_view_stats))
                }
                TextButton(
                    onClick = onRemoveFriend,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(stringResource(R.string.social_friend_options_remove), color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_close))
            }
        }
    )
}

// Data Classes & Sealed Interface
data class SocialUiState(
    val isLoading: Boolean = false,
    val friends: List<Friend> = emptyList(),
    val newFriendUsername: String = "",
    val isAddingFriend: Boolean = false,
    val addFriendError: String? = null,
    val error: String? = null
)

data class Friend(
    val id: String,
    val username: String,
    val streak: Int,
    val currentPleasure: FriendPleasure? = null
)

data class FriendPleasure(
    val title: String,
    val status: PleasureStatus
)

enum class PleasureStatus {
    IN_PROGRESS,
    COMPLETED
}

sealed interface SocialEvent {
    data class OnUsernameChanged(val username: String) : SocialEvent
    data object OnAddFriendClicked : SocialEvent
    data class OnRemoveFriend(val friend: Friend) : SocialEvent
    data class OnViewFriendStats(val friend: Friend) : SocialEvent
    data object OnRetryClicked : SocialEvent
}

// Preview Data
private val previewFriends = listOf(
    Friend(
        id = "1",
        username = "Dams",
        streak = 8,
        currentPleasure = FriendPleasure(
            title = "Sortie au restaurant",
            status = PleasureStatus.COMPLETED
        )
    ),
    Friend(
        id = "2",
        username = "Emma",
        streak = 326,
        currentPleasure = FriendPleasure(
            title = "Baiser Hugo",
            status = PleasureStatus.IN_PROGRESS
        )
    ),
    Friend(
        id = "3",
        username = "Alisson",
        streak = 2,
        currentPleasure = FriendPleasure(
            title = "Faire une bouffe XXL",
            status = PleasureStatus.IN_PROGRESS
        )
    ),
    Friend(
        id = "4",
        username = "Lilou la fripouille",
        streak = 33,
        currentPleasure = FriendPleasure(
            title = "Appeler mon petit frère",
            status = PleasureStatus.COMPLETED
        )
    )
)

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
