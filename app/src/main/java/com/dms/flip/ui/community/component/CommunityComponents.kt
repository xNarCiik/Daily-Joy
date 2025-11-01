package com.dms.flip.ui.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.dms.flip.R
import com.dms.flip.ui.community.*

/**
 * Bottom Sheet pour les actions sur un post
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostMenuBottomSheet(
    post: FriendPost,
    onDismiss: () -> Unit,
    onReport: () -> Unit,
    onHide: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Text(
                text = "Actions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Hide post
            MenuOption(
                icon = Icons.Default.VisibilityOff,
                title = stringResource(R.string.menu_hide_post),
                subtitle = stringResource(R.string.menu_hide_post_desc),
                onClick = {
                    onHide()
                    onDismiss()
                }
            )

            // Report post
            MenuOption(
                icon = Icons.Default.Flag,
                title = stringResource(R.string.menu_report),
                subtitle = stringResource(R.string.menu_report_desc),
                onClick = {
                    onReport()
                    onDismiss()
                },
                isDestructive = true
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Option de menu dans un Bottom Sheet
 */
@Composable
private fun MenuOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isDestructive) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Carte pour une suggestion d'ami
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SuggestionItemCard(
    suggestion: FriendSuggestion,
    onAdd: () -> Unit,
    onHide: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar
                if (suggestion.avatarUrl != null) {
                    GlideImage(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        model = suggestion.avatarUrl,
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
                            text = suggestion.username.firstOrNull()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = suggestion.username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = suggestion.handle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (suggestion.mutualFriendsCount > 0) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.People,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = stringResource(
                                    R.string.mutual_friends_count,
                                    suggestion.mutualFriendsCount
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Actions
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = onAdd,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.button_add),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = onHide,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.button_hide),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Carte pour une demande d'ami reçue
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReceivedRequestCard(
    request: FriendRequest,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = request.username.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = request.username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = request.handle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatRequestTime(request.requestedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onDecline,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.button_decline),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.button_accept),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Carte pour une demande d'ami envoyée
 */
@Composable
fun SentRequestCard(
    request: FriendRequest,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = request.username.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column {
                    Text(
                        text = request.username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.request_pending),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            TextButton(
                onClick = onCancel,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.button_cancel),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

/**
 * Filtres pour les suggestions
 */
@Composable
fun SuggestionFiltersRow(
    selectedFilter: SuggestionFilter,
    onFilterSelected: (SuggestionFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == SuggestionFilter.ALL,
            onClick = { onFilterSelected(SuggestionFilter.ALL) },
            label = {
                Text(
                    text = stringResource(R.string.filter_all),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            leadingIcon = if (selectedFilter == SuggestionFilter.ALL) {
                { Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp)) }
            } else null
        )
        FilterChip(
            selected = selectedFilter == SuggestionFilter.COMMUNITIES,
            onClick = { onFilterSelected(SuggestionFilter.COMMUNITIES) },
            label = {
                Text(
                    text = stringResource(R.string.filter_communities),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            leadingIcon = if (selectedFilter == SuggestionFilter.COMMUNITIES) {
                { Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp)) }
            } else null
        )
        FilterChip(
            selected = selectedFilter == SuggestionFilter.CONTACTS,
            onClick = { onFilterSelected(SuggestionFilter.CONTACTS) },
            label = {
                Text(
                    text = stringResource(R.string.filter_contacts),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            leadingIcon = if (selectedFilter == SuggestionFilter.CONTACTS) {
                { Icon(Icons.Default.Check, contentDescription = null, Modifier.size(16.dp)) }
            } else null
        )
    }
}

/**
 * Empty States
 */
@Composable
fun EmptySuggestionsState() {
    EmptyStateTemplate(
        icon = Icons.Default.PersonSearch,
        title = stringResource(R.string.empty_suggestions_title),
        message = stringResource(R.string.empty_suggestions_message)
    )
}

@Composable
fun EmptyInvitationsState() {
    EmptyStateTemplate(
        icon = Icons.Default.MailOutline,
        title = stringResource(R.string.empty_invitations_title),
        message = stringResource(R.string.empty_invitations_message)
    )
}

@Composable
fun EmptySentRequestsState() {
    EmptyStateTemplate(
        icon = Icons.Default.Send,
        title = stringResource(R.string.empty_sent_requests_title),
        message = stringResource(R.string.empty_sent_requests_message)
    )
}

/**
 * Template générique pour les empty states
 */
@Composable
private fun EmptyStateTemplate(
    icon: ImageVector,
    title: String,
    message: String
) {
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
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Utility functions
private fun formatRequestTime(timestamp: Long): String {
    val days = (System.currentTimeMillis() - timestamp) / (1000 * 60 * 60 * 24)
    return when {
        days == 0L -> "Aujourd'hui"
        days == 1L -> "Hier"
        days < 7 -> "Il y a ${days}j"
        else -> "Il y a ${days / 7} sem"
    }
}