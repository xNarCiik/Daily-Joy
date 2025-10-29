package com.dms.flip.ui.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dms.flip.R
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.ui.social.component.FriendRequestDrawer
import com.dms.flip.ui.social.component.dialog.FriendOptionsDialog
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewFriends
import kotlinx.coroutines.launch

@Composable
fun SocialScreen(
    modifier: Modifier = Modifier,
    uiState: SocialUiState,
    onEvent: (SocialEvent) -> Unit
) {
    var showFriendOptionsDialog by remember { mutableStateOf<Friend?>(null) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            FriendRequestDrawer(
                pendingRequests = uiState.pendingRequests,
                sentRequests = uiState.sentRequests,
                onAddFriend = { username ->
                    onEvent(SocialEvent.OnAddFriend(username))
                },
                onAcceptRequest = { request ->
                    onEvent(SocialEvent.OnAcceptFriendRequest(request))
                },
                onDeclineRequest = { request ->
                    onEvent(SocialEvent.OnDeclineFriendRequest(request))
                },
                onCancelRequest = { request ->
                    // TODO: Lier au ViewModel (viewModel.onCancelFriendRequest(request))
                },
                onClose = {
                    scope.launch { drawerState.close() }
                }
            )
        },
        gesturesEnabled = true
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top Bar // TODO Refacto FlipTopBar
            SocialTopBar(
                pendingRequestsCount = uiState.pendingRequests.size,
                onRequestsClick = {
                    scope.launch { drawerState.open() }
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Invitations section
                if (uiState.pleasureInvitations.isNotEmpty()) {
                    item {
                        PleasureInvitationsSection(
                            invitations = uiState.pleasureInvitations,
                            onAccept = { onEvent(SocialEvent.OnAcceptInvitation(it)) },
                            onDecline = { onEvent(SocialEvent.OnDeclineInvitation(it)) }
                        )
                    }
                }

                // Search bar
                item {
                    SearchBar(
                        searchQuery = uiState.searchQuery,
                        onSearchQueryChange = { onEvent(SocialEvent.OnSearchQueryChanged(it)) }
                    )
                }

                // Friends list
                if (uiState.friends.isEmpty()) {
                    item {
                        EmptyFriendsState()
                    }
                } else {
                    items(
                        items = uiState.friends,
                        key = { it.id }
                    ) { friend ->
                        FriendItemCard(
                            friend = friend,
                            onClick = { showFriendOptionsDialog = friend }
                        )
                    }
                }
            }
        }

        // Friend options dialog
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
private fun SocialTopBar(
    pendingRequestsCount: Int,
    onRequestsClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bouton Start
            IconButton(
                onClick = { },
                modifier = Modifier.size(48.dp),
                enabled = false
            ) {
            }

            // Titre centré
            Text(
                text = "Mes amis",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            // Friend requests button with badge
            if (pendingRequestsCount > 0) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ) {
                            Text(
                                text = pendingRequestsCount.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                ) {
                    IconButton(
                        onClick = onRequestsClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = stringResource(R.string.social_friend_requests),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            } else {
                IconButton(
                    onClick = onRequestsClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = stringResource(R.string.social_friend_requests),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (searchQuery.isEmpty()) {
                    Text(
                        text = stringResource(R.string.social_search_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun PleasureInvitationsSection(
    invitations: List<PleasureInvitation>,
    onAccept: (PleasureInvitation) -> Unit,
    onDecline: (PleasureInvitation) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.social_invitations_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            invitations.forEach { invitation ->
                PleasureInvitationCard(
                    invitation = invitation,
                    onAccept = { onAccept(invitation) },
                    onDecline = { onDecline(invitation) }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PleasureInvitationCard(
    invitation: PleasureInvitation,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Friend info with avatar and pleasure
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            if (invitation.friendAvatarUrl != null) {
                GlideImage(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    model = invitation.friendAvatarUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = invitation.friendName.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Text content with pleasure
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(
                        R.string.social_invitation_from,
                        invitation.friendName
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Category icon with tint
                    Icon(
                        imageVector = invitation.pleasureCategory.icon,
                        contentDescription = null,
                        tint = invitation.pleasureCategory.iconTint,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = invitation.pleasureTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onAccept,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.social_accept),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Button(
                onClick = onDecline,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.social_decline),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FriendItemCard(
    friend: Friend,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar with online status
        Box {
            if (friend.avatarUrl != null) {
                GlideImage(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    model = friend.avatarUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = friend.username.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Online status indicator (only if online)
            if (friend.isOnline) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiary)
                    )
                }
            }
        }

        // Friend info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = friend.username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Current pleasure with emoji and category icon WITH TINT
            friend.currentPleasure?.let { pleasure ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Category icon with its specific tint
                    Icon(
                        imageVector = pleasure.category.icon,
                        contentDescription = null,
                        tint = pleasure.category.iconTint,
                        modifier = Modifier.size(16.dp)
                    )

                    // Pleasure text
                    Text(
                        text = pleasure.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            } ?: run {
                // No pleasure yet
                Text(
                    text = stringResource(R.string.social_no_pleasure_today),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }

        // Pleasure status indicator (only if has pleasure)
        friend.currentPleasure?.let { pleasure ->
            PleasureStatusBadge(status = pleasure.status)
        }
    }
}

@Composable
private fun PleasureStatusBadge(status: PleasureStatus) {
    val isCompleted = status == PleasureStatus.COMPLETED

    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(
                if (isCompleted)
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                else
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.HourglassEmpty,
            contentDescription = null,
            tint = if (isCompleted)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(12.dp)
        )

        Text(
            text = if (isCompleted)
                stringResource(R.string.status_completed_short)
            else
                stringResource(R.string.status_in_progress),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isCompleted)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun EmptyFriendsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp, horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        }

        Text(
            text = stringResource(R.string.social_empty_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.social_empty_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@LightDarkPreview
@Composable
private fun SocialScreenPreview() {
    FlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SocialScreen(
                uiState = SocialUiState(
                    friends = previewFriends,
                    pleasureInvitations = listOf(
                        PleasureInvitation(
                            id = "1",
                            friendId = "1",
                            friendName = "Léa Martin",
                            friendAvatarUrl = null,
                            pleasureCategory = PleasureCategory.SOCIAL,
                            pleasureTitle = "Un café au soleil ce matin"
                        )
                    ),
                    pendingRequests = emptyList(),
                    searchQuery = "",
                    isSearching = false
                ),
                onEvent = {}
            )
        }
    }
}
