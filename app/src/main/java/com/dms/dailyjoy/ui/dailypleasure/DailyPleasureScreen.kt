package com.dms.dailyjoy.ui.dailypleasure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.component.AppHeader
import com.dms.dailyjoy.ui.component.LoadingState
import com.dms.dailyjoy.ui.dailypleasure.component.DailyPleasureCompletedContent
import com.dms.dailyjoy.ui.dailypleasure.component.DailyPleasureContent
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasureUiState

@Composable
fun DailyPleasureScreen(
    modifier: Modifier = Modifier,
    uiState: DailyPleasureUiState,
    onEvent: (DailyPleasureEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        AppHeader(
            title = stringResource(R.string.daily_pleasure_title),
            subtitle = uiState.headerMessage,
            icon = Icons.Default.EmojiEvents
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }

                uiState.dailyPleasure.isDone -> {
                    DailyPleasureCompletedContent()
                }

                else -> {
                    DailyPleasureContent(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureNotCompletedScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = previewDailyPleasureUiState,
                onEvent = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureCompletedScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = previewDailyPleasureUiState.copy(
                    dailyPleasure = previewDailyPleasureUiState.dailyPleasure.copy(isDone = true)
                ),
                onEvent = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureFlippedScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = previewDailyPleasureUiState.copy(
                    dailyPleasure = previewDailyPleasureUiState.dailyPleasure.copy(
                        isFlipped = true
                    )
                ),
                onEvent = {}
            )
        }
    }
}
