package com.dms.dailyjoy.ui.dailypleasure

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.ui.component.AppHeader
import com.dms.dailyjoy.ui.component.LoadingState
import com.dms.dailyjoy.ui.dailypleasure.component.DailyPleasureCompletedContent
import com.dms.dailyjoy.ui.dailypleasure.component.DailyPleasureContent
import com.dms.dailyjoy.ui.dailypleasure.component.DailyPleasureSetupContent
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasure

@Composable
fun DailyPleasureScreen(
    modifier: Modifier = Modifier,
    uiState: DailyPleasureUiState,
    onEvent: (DailyPleasureEvent) -> Unit = {},
    navigateToManagePleasures: () -> Unit = {}
) {
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
            AnimatedContent(
                targetState = uiState.screenState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                },
                label = "screenStateTransition"
            ) { state ->
                when (state) {
                    is DailyPleasureScreenState.Loading -> {
                        LoadingState()
                    }

                    is DailyPleasureScreenState.SetupRequired -> {
                        DailyPleasureSetupContent(
                            currentPleasureCount = state.pleasureCount,
                            requiredCount = MinimumPleasuresCount,
                            onConfigureClick = navigateToManagePleasures
                        )
                    }

                    is DailyPleasureScreenState.Ready -> {
                        DailyPleasureContent(
                            uiState = state,
                            onEvent = onEvent
                        )
                    }

                    is DailyPleasureScreenState.Completed -> {
                        DailyPleasureCompletedContent()
                    }
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureSetupScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = DailyPleasureUiState(
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
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = DailyPleasureUiState(
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
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(uiState = DailyPleasureUiState(screenState = DailyPleasureScreenState.Completed))
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureFlippedScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = DailyPleasureUiState(
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
