package com.dms.dailyjoy.ui.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.history.components.HistoryGrid
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    AnimatedContent(
        targetState = uiState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "HistoryScreenAnimation"
    ) { state ->
        Box(modifier = modifier.fillMaxSize()) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    // TODO: Implémenter un écran d'erreur avec un bouton 'Réessayer'
                    Text(
                        text = "Erreur: ${state.error}",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.isEmpty -> {
                    Text(
                        text = stringResource(R.string.history_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(32.dp)
                            .align(Alignment.Center)
                    )
                }

                else -> {
                    HistoryContent(
                        items = state.weeklyPleasures,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    items: List<WeeklyPleasureItem>,
    onEvent: (HistoryEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HistoryGrid(
            items = items,
            onCardClicked = { item -> onEvent(HistoryEvent.OnCardClicked(item)) }
        )
    }
}

@LightDarkPreview
@Composable
private fun HistoryPreview() {
    DailyJoyTheme {
        HistoryScreen(
            uiState = HistoryUiState(),
            onEvent = {}
        )
    }
}
