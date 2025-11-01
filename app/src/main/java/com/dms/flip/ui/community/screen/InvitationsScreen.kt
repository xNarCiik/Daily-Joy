package com.dms.flip.ui.community.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import com.dms.flip.ui.community.CommunityEvent
import com.dms.flip.ui.community.CommunityUiState
import com.dms.flip.ui.community.component.InvitationsTopBar
import com.dms.flip.ui.community.component.ReceivedRequestItem
import com.dms.flip.ui.community.component.SentRequestItem
import com.dms.flip.ui.community.component.SuggestionsSection
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewCommunityUiStateFull

@Composable
fun InvitationsScreen(
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
        InvitationsTopBar(onNavigateBack = onNavigateBack)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (uiState.pendingRequests.isNotEmpty()) {
                item {
                    Column {
                        uiState.pendingRequests.forEach { request ->
                            ReceivedRequestItem(
                                request = request,
                                onAccept = { onEvent(CommunityEvent.OnAcceptFriendRequest(request)) },
                                onDecline = { onEvent(CommunityEvent.OnDeclineFriendRequest(request)) },
                                onClick = { onEvent(CommunityEvent.OnViewProfile(request.userId)) }
                            )
                        }
                    }
                }
            }

            if (uiState.sentRequests.isNotEmpty()) {
                item {
                    Column {
                        uiState.sentRequests.forEach { request ->
                            SentRequestItem(
                                request = request,
                                onCancel = { onEvent(CommunityEvent.OnCancelSentRequest(request)) },
                                onClick = { onEvent(CommunityEvent.OnViewProfile(request.userId)) }
                            )
                        }
                    }
                }
            }

            if (uiState.suggestions.isNotEmpty()) {
                item {
                    SuggestionsSection(
                        suggestions = uiState.suggestions,
                        onAdd = { onEvent(CommunityEvent.OnAddSuggestion(it)) },
                        onHide = { onEvent(CommunityEvent.OnHideSuggestion(it)) },
                        onSuggestionClick = { onEvent(CommunityEvent.OnViewProfile(it.id)) }
                    )
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun InvitationsScreenPreview() {
    FlipTheme {
        Surface{
            InvitationsScreen(
                uiState = previewCommunityUiStateFull,
                onEvent = {},
                onNavigateBack = {}
            )
        }
    }
}
