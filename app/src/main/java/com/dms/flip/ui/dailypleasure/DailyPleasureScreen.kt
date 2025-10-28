package com.dms.flip.ui.dailypleasure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.ui.component.LoadingState
import com.dms.flip.ui.dailypleasure.component.DailyPleasureCompletedContent
import com.dms.flip.ui.dailypleasure.component.DailyPleasureContent
import com.dms.flip.ui.dailypleasure.component.DailyPleasureSetupContent
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewDailyPleasure

@Composable
fun DailyPleasureScreen(
    modifier: Modifier = Modifier,
    uiState: DailyPleasureUiState,
    onEvent: (DailyPleasureEvent) -> Unit = {},
    navigateToManagePleasures: () -> Unit = {}
) {
    val screenState = uiState.screenState

    // Observe lifecycle events to refresh the state when the screen is resumed
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onEvent(DailyPleasureEvent.Reload)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 22.dp),
    ) {
        // Header Message
        if (uiState.headerMessage.isNotBlank()) {
            HeaderMessage(message = uiState.headerMessage)
        }

        val contentModifier = Modifier
            .fillMaxWidth()
            .weight(1f)

        if (screenState is DailyPleasureScreenState.Loading) {
            LoadingState(modifier = contentModifier)
            return@Column
        }

        Box(
            modifier = contentModifier,
            contentAlignment = Alignment.Center
        ) {
            when (screenState) {
                is DailyPleasureScreenState.SetupRequired -> {
                    DailyPleasureSetupContent(
                        currentPleasureCount = screenState.pleasureCount,
                        requiredCount = MinimumPleasuresCount,
                        onConfigureClick = navigateToManagePleasures
                    )
                }

                is DailyPleasureScreenState.Ready -> {
                    DailyPleasureContent(
                        uiState = screenState,
                        onEvent = onEvent
                    )
                }

                is DailyPleasureScreenState.Completed -> {
                    DailyPleasureCompletedContent()
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun HeaderMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.titleLarge.lineHeight * 1.2
        )
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureSetupScreenPreview() {
    FlipTheme {
        Surface {
            DailyPleasureScreen(
                uiState = DailyPleasureUiState(
                    headerMessage = "Prenez le temps de savourer les petits plaisirs de la vie 🌟",
                    screenState = DailyPleasureScreenState.SetupRequired(
                        pleasureCount = 1
                    )
                )
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureNotCompletedScreenPreview() {
    FlipTheme {
        Surface {
            DailyPleasureScreen(
                uiState = DailyPleasureUiState(
                    headerMessage = "Chaque petit moment de bonheur compte ✨",
                    screenState = DailyPleasureScreenState.Ready(
                        availableCategories = PleasureCategory.entries,
                        dailyPleasure = previewDailyPleasure
                    )
                )
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureCompletedScreenPreview() {
    FlipTheme {
        Surface {
            DailyPleasureScreen(
                uiState = DailyPleasureUiState(
                    headerMessage = "Félicitations pour cette belle journée ! 🎉",
                    screenState = DailyPleasureScreenState.Completed
                )
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureFlippedScreenPreview() {
    FlipTheme {
        Surface {
            DailyPleasureScreen(
                uiState = DailyPleasureUiState(
                    headerMessage = "La joie est dans les détails du quotidien 💫",
                    screenState = DailyPleasureScreenState.Ready(
                        availableCategories = PleasureCategory.entries,
                        dailyPleasure = previewDailyPleasure,
                        isCardFlipped = true
                    )
                )
            )
        }
    }
}