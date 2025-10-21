package com.dms.dailyjoy.ui.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.history.component.HistoryGrid
import com.dms.dailyjoy.ui.history.component.PleasureCard
import com.dms.dailyjoy.ui.history.component.WeeklyProgress
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewHistoryUiState

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.error != null -> {
                // TODO: Implémenter un écran d'erreur avec un bouton 'Réessayer'
                Text(
                    text = "Erreur: ${uiState.error}",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.isEmpty -> {
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
                    uiState = uiState,
                    onEvent = onEvent
                )
            }
        }
    }

    AnimatedVisibility(
        visible = uiState.selectedPleasure != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                .clickable { onEvent(HistoryEvent.OnBottomSheetDismissed) },
            contentAlignment = Alignment.Center
        ) {
            uiState.selectedPleasure?.let {
                PleasureCard(modifier = Modifier.clickable(enabled = false) {}, pleasure = it)
            }
        }
    }
}

@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WeeklyProgress(completedCount = uiState.completedPleasuresCount)

        if (uiState.remainingPleasuresCount > 0) {
            Spacer(modifier = Modifier.height(26.dp))

            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = pluralStringResource(
                    id = R.plurals.remaining_pleasures_text,
                    count = uiState.remainingPleasuresCount,
                    uiState.remainingPleasuresCount
                ),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HistoryGrid(
            items = uiState.weeklyPleasures,
            onCardClicked = { item -> onEvent(HistoryEvent.OnCardClicked(item)) }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@LightDarkPreview
@Composable
private fun HistoryEmptyPreview() {
    DailyJoyTheme {
        HistoryScreen(
            uiState = HistoryUiState(),
            onEvent = {}
        )
    }
}

@LightDarkPreview
@Composable
private fun HistoryPreview() {
    DailyJoyTheme {
        HistoryScreen(
            uiState = previewHistoryUiState,
            onEvent = {}
        )
    }
}
