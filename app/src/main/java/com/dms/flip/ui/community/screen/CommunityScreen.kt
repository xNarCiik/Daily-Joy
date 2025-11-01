package com.dms.flip.ui.community.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Badge
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dms.flip.R
import com.dms.flip.ui.community.CommunityEvent
import com.dms.flip.ui.community.CommunityTab
import com.dms.flip.ui.community.CommunityUiState
import com.dms.flip.ui.community.Friend
import com.dms.flip.ui.community.FriendPost
import com.dms.flip.ui.community.SuggestionFilter
import com.dms.flip.ui.community.SuggestionSource
import com.dms.flip.ui.community.components.EmptyInvitationsState
import com.dms.flip.ui.community.components.EmptySentRequestsState
import com.dms.flip.ui.community.components.EmptySuggestionsState
import com.dms.flip.ui.community.components.PostMenuBottomSheet
import com.dms.flip.ui.community.components.ReceivedRequestCard
import com.dms.flip.ui.community.components.SentRequestCard
import com.dms.flip.ui.community.components.SuggestionFiltersRow
import com.dms.flip.ui.community.components.SuggestionItemCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val FireStreakColor = Color(0xFFFF6B35)

@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    uiState: CommunityUiState,
    onEvent: (CommunityEvent) -> Unit
) {
    var showPostMenu by remember { mutableStateOf<FriendPost?>(null) }
    var showFriendMenu by remember { mutableStateOf<Friend?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CommunityTopBar(
                onSearchClick = { onEvent(CommunityEvent.OnSearchClicked) },
                onAddFriendClick = { onEvent(CommunityEvent.OnAddFriendClicked) }
            )

            CommunityTabs(
                selectedTab = uiState.selectedTab,
                onTabSelected = { onEvent(CommunityEvent.OnTabSelected(it)) }
            )

            when (uiState.selectedTab) {
                CommunityTab.FRIENDS -> {
                    FriendsFeedContent(
                        posts = uiState.friendsPosts,
                        onEvent = onEvent,
                        onShowMenu = { showPostMenu = it }
                    )
                }
                CommunityTab.SUGGESTIONS -> {
                    SuggestionsTabContent(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
                CommunityTab.INVITATIONS -> {
                    InvitationsTabContent(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
            }
        }

        // FAB pour créer un post (uniquement sur l'onglet Amis)
        AnimatedVisibility(
            visible = uiState.selectedTab == CommunityTab.FRIENDS,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            FloatingActionButton(
                onClick = { onEvent(CommunityEvent.OnCreatePostClicked) },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.community_create_post)
                )
            }
        }

        // Bottom Sheet pour le menu des posts
        if (showPostMenu != null) {
            PostMenuBottomSheet(
                post = showPostMenu!!,
                onDismiss = { showPostMenu = null },
                onReport = {
                    // TODO: Implémenter la fonctionnalité de signalement
                    showPostMenu = null
                },
                onHide = {
                    // TODO: Implémenter le masquage du post
                    showPostMenu = null
                }
            )
        }
    }
}

@Composable
private fun CommunityTopBar(
    onSearchClick: () -> Unit,
    onAddFriendClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.community_search),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = stringResource(R.string.community_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onAddFriendClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = stringResource(R.string.community_add_friend),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun CommunityTabs(
    selectedTab: CommunityTab,
    onTabSelected: (CommunityTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                color = MaterialTheme.colorScheme.primary,
                height = 3.dp
            )
        }
    ) {
        Tab(
            selected = selectedTab == CommunityTab.FRIENDS,
            onClick = { onTabSelected(CommunityTab.FRIENDS) },
            text = {
                Text(
                    text = stringResource(R.string.community_tab_friends),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (selectedTab == CommunityTab.FRIENDS)
                        FontWeight.Bold else FontWeight.Medium
                )
            }
        )
        Tab(
            selected = selectedTab == CommunityTab.SUGGESTIONS,
            onClick = { onTabSelected(CommunityTab.SUGGESTIONS) },
            text = {
                Text(
                    text = stringResource(R.string.community_tab_suggestions),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (selectedTab == CommunityTab.SUGGESTIONS)
                        FontWeight.Bold else FontWeight.Medium
                )
            }
        )
        Tab(
            selected = selectedTab == CommunityTab.INVITATIONS,
            onClick = { onTabSelected(CommunityTab.INVITATIONS) },
            text = {
                Text(
                    text = stringResource(R.string.community_tab_invitations),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (selectedTab == CommunityTab.INVITATIONS)
                        FontWeight.Bold else FontWeight.Medium
                )
            }
        )
    }
}

@Composable
private fun FriendsFeedContent(
    posts: List<FriendPost>,
    onEvent: (CommunityEvent) -> Unit,
    onShowMenu: (FriendPost) -> Unit
) {
    if (posts.isEmpty()) {
        EmptyFeedState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = posts,
                key = { it.id }
            ) { post ->
                PostCard(
                    post = post,
                    onLike = { onEvent(CommunityEvent.OnPostLiked(post.id)) },
                    onComment = { onEvent(CommunityEvent.OnPostCommentClicked(post)) },
                    onMenu = { onShowMenu(post) },
                    onProfileClick = { onEvent(CommunityEvent.OnFriendClicked(post.friend)) }
                )
            }
        }
    }
}

@Composable
private fun SuggestionsTabContent(
    uiState: CommunityUiState,
    onEvent: (CommunityEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Filters
        SuggestionFiltersRow(
            selectedFilter = uiState.suggestionFilter,
            onFilterSelected = { onEvent(CommunityEvent.OnSuggestionFilterChanged(it)) }
        )

        val filteredSuggestions = when (uiState.suggestionFilter) {
            SuggestionFilter.ALL -> uiState.suggestions
            SuggestionFilter.COMMUNITIES -> uiState.suggestions.filter {
                it.source == SuggestionSource.COMMUNITY
            }
            SuggestionFilter.CONTACTS -> uiState.suggestions.filter {
                it.source == SuggestionSource.CONTACTS
            }
        }

        if (filteredSuggestions.isEmpty()) {
            EmptySuggestionsState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(filteredSuggestions, key = { it.id }) { suggestion ->
                    SuggestionItemCard(
                        suggestion = suggestion,
                        onAdd = { onEvent(CommunityEvent.OnAddSuggestion(suggestion)) },
                        onHide = { onEvent(CommunityEvent.OnHideSuggestion(suggestion)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun InvitationsTabContent(
    uiState: CommunityUiState,
    onEvent: (CommunityEvent) -> Unit
) {
    var selectedSubTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Sub-tabs pour Reçues / Envoyées
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.requests_received))
                        if (uiState.pendingRequests.isNotEmpty()) {
                            Badge {
                                Text(uiState.pendingRequests.size.toString())
                            }
                        }
                    }
                }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text(stringResource(R.string.requests_sent)) }
            )
        }

        // Content
        when (selectedSubTab) {
            0 -> {
                if (uiState.pendingRequests.isEmpty()) {
                    EmptyInvitationsState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(uiState.pendingRequests, key = { it.id }) { request ->
                            ReceivedRequestCard(
                                request = request,
                                onAccept = {
                                    onEvent(CommunityEvent.OnAcceptFriendRequest(request))
                                },
                                onDecline = {
                                    onEvent(CommunityEvent.OnDeclineFriendRequest(request))
                                }
                            )
                        }
                    }
                }
            }
            1 -> {
                if (uiState.sentRequests.isEmpty()) {
                    EmptySentRequestsState()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(uiState.sentRequests, key = { it.id }) { request ->
                            SentRequestCard(
                                request = request,
                                onCancel = {
                                    onEvent(CommunityEvent.OnCancelSentRequest(request))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PostCard(
    post: FriendPost,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onMenu: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onProfileClick)
            ) {
                // Avatar
                UserAvatar(
                    username = post.friend.username,
                    avatarUrl = post.friend.avatarUrl,
                    size = 48.dp
                )

                // Name + Handle + Streak
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = post.friend.username,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (post.friend.streak > 0) {
                            StreakBadge(streak = post.friend.streak)
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = post.friend.handle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = formatTimestamp(post.timestamp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(
                onClick = onMenu,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Content
        Text(
            text = post.content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 60.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PostActionButton(
                icon = if (post.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                count = post.likesCount,
                isActive = post.isLiked,
                onClick = onLike
            )

            PostActionButton(
                icon = Icons.Default.ChatBubbleOutline,
                count = post.commentsCount,
                isActive = false,
                onClick = onComment
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun UserAvatar(
    username: String,
    avatarUrl: String?,
    size: Dp,
    modifier: Modifier = Modifier
) {
    if (avatarUrl != null) {
        GlideImage(
            modifier = modifier
                .size(size)
                .clip(CircleShape),
            model = avatarUrl,
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    } else {
        Box(
            modifier = modifier
                .size(size)
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
                text = username.firstOrNull()?.uppercase() ?: "?",
                style = when {
                    size >= 56.dp -> MaterialTheme.typography.titleLarge
                    else -> MaterialTheme.typography.titleMedium
                },
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun StreakBadge(streak: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(FireStreakColor.copy(alpha = 0.15f))
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocalFireDepartment,
            contentDescription = null,
            tint = FireStreakColor,
            modifier = Modifier.size(10.dp)
        )
        Text(
            text = streak.toString(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = FireStreakColor
        )
    }
}

@Composable
private fun PostActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isActive) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Empty States
@Composable
private fun EmptyFeedState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Text(
                text = "Aucune activité pour le moment",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Ajoutez des amis pour voir leurs activités",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// [Autres empty states à ajouter : EmptySuggestionsState, EmptyInvitationsState, etc.]

private fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60000 -> "à l'instant"
        diff < 3600000 -> "${diff / 60000}min"
        diff < 86400000 -> "${diff / 3600000}h"
        else -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(timestamp))
    }
}