package com.dms.flip.ui.community.screen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.dms.flip.R
import com.dms.flip.ui.community.CommunityEvent
import com.dms.flip.ui.community.CommunityUiState
import com.dms.flip.ui.community.RelationshipStatus
import com.dms.flip.ui.community.UserSearchResult

@Composable
fun SearchFriendsScreen(
    modifier: Modifier = Modifier,
    uiState: CommunityUiState,
    onEvent: (CommunityEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar with Search
        SearchTopBar(
            searchQuery = uiState.searchQuery,
            onQueryChange = { onEvent(CommunityEvent.OnSearchQueryChanged(it)) },
            onNavigateBack = onNavigateBack
        )

        // Search Results
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(uiState.searchResults, key = { it.id }) { result ->
                SearchResultItem(
                    result = result,
                    onAdd = { onEvent(CommunityEvent.OnAddUserFromSearch(result.id)) },
                    onClick = { onEvent(CommunityEvent.OnSearchResultClicked(result)) }
                )
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        BasicTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                stringResource(R.string.search_placeholder),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f)
                            )
                        }
                        innerTextField()
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SearchResultItem(
    result: UserSearchResult,
    onAdd: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = result.username.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column {
                Text(
                    text = result.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = result.handle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        when (result.relationshipStatus) {
            RelationshipStatus.NONE -> {
                Button(
                    onClick = onAdd,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(stringResource(R.string.button_add))
                }
            }
            RelationshipStatus.PENDING_SENT -> {
                Text(
                    text = stringResource(R.string.status_pending),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            RelationshipStatus.FRIEND -> {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Friend",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            else -> {}
        }
    }
}
