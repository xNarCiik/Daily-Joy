package com.dms.flip.ui.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.dms.flip.R
import com.dms.flip.ui.community.CommunityEvent
import com.dms.flip.ui.community.CommunityUiState
import com.dms.flip.ui.community.FriendSuggestion
import com.dms.flip.ui.community.SuggestionFilter
import com.dms.flip.ui.community.SuggestionSource

@Composable
fun SuggestionsScreen(
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
        // Top Bar
        SuggestionsTopBar(
            onNavigateBack = onNavigateBack,
            onSearchClick = { onEvent(CommunityEvent.OnSearchClicked) }
        )

        // Filters
        SuggestionFilters(
            selectedFilter = uiState.suggestionFilter,
            onFilterSelected = { onEvent(CommunityEvent.OnSuggestionFilterChanged(it)) }
        )

        // Suggestions List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            val filteredSuggestions = when (uiState.suggestionFilter) {
                SuggestionFilter.ALL -> uiState.suggestions
                SuggestionFilter.COMMUNITIES -> uiState.suggestions.filter { it.source == SuggestionSource.COMMUNITY }
                SuggestionFilter.CONTACTS -> uiState.suggestions.filter { it.source == SuggestionSource.CONTACTS }
            }

            items(
                items = filteredSuggestions,
                key = { it.id }
            ) { suggestion ->
                SuggestionItem(
                    suggestion = suggestion,
                    onAdd = { onEvent(CommunityEvent.OnAddSuggestion(suggestion)) },
                    onHide = { onEvent(CommunityEvent.OnHideSuggestion(suggestion)) }
                )
            }
        }
    }
}

@Composable
private fun SuggestionsTopBar(
    onNavigateBack: () -> Unit,
    onSearchClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.navigate_back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = stringResource(R.string.suggestions_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.community_search),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun SuggestionFilters(
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
            label = { Text(stringResource(R.string.filter_all)) }
        )
        FilterChip(
            selected = selectedFilter == SuggestionFilter.COMMUNITIES,
            onClick = { onFilterSelected(SuggestionFilter.COMMUNITIES) },
            label = { Text(stringResource(R.string.filter_communities)) }
        )
        FilterChip(
            selected = selectedFilter == SuggestionFilter.CONTACTS,
            onClick = { onFilterSelected(SuggestionFilter.CONTACTS) },
            label = { Text(stringResource(R.string.filter_contacts)) }
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SuggestionItem(
    suggestion: FriendSuggestion,
    onAdd: () -> Unit,
    onHide: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Avatar
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

            Column {
                Text(
                    text = suggestion.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = suggestion.handle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (suggestion.mutualFriendsCount > 0) {
                    Text(
                        text = stringResource(R.string.mutual_friends_count, suggestion.mutualFriendsCount),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Hide
            OutlinedButton(
                onClick = onHide,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(stringResource(R.string.button_hide))
            }

            // Add
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
    }
}
