package com.dms.dailyjoy.ui.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureScreen
import com.dms.dailyjoy.ui.dailypleasure.PleasureViewModel
import com.dms.dailyjoy.ui.history.HistoryScreen
import com.dms.dailyjoy.ui.settings.SettingsScreen
import com.dms.dailyjoy.ui.settings.SettingsViewModel
import com.dms.dailyjoy.ui.settings.manage.ManagePleasuresScreen
import com.dms.dailyjoy.ui.util.navigationAnimationDuration
import kotlinx.serialization.Serializable

@Serializable
object DailyPleasureRoute

@Serializable
object HistoryRoute

@Serializable
object SettingsRoute

@Serializable
object ManagePleasuresRoute

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    val modifierWithPaddingValues = Modifier.padding(paddingValues)
    NavHost(
        navController = navController,
        startDestination = DailyPleasureRoute
    ) {
        composable<DailyPleasureRoute> {
            val viewModel: PleasureViewModel = hiltViewModel()
            val dailyPleasureState by viewModel.state.collectAsState()
            DailyPleasureScreen(
                modifier = modifierWithPaddingValues,
                dailyPleasureState = dailyPleasureState,
                onCardFlipped = viewModel::onDailyCardFlipped,
                onDonePleasure = viewModel::markDailyCardAsDone
            )
        }

        composable<HistoryRoute> {
            Box(modifier = Modifier.padding(paddingValues)) {
                HistoryScreen(
                    modifier = modifierWithPaddingValues
                )
            }
        }

        composable<SettingsRoute> {
            val viewModel: SettingsViewModel = hiltViewModel()
            val theme by viewModel.theme.collectAsState()
            val dailyReminderEnabled by viewModel.dailyReminderEnabled.collectAsState()
            val reminderTime by viewModel.reminderTime.collectAsState()

            SettingsScreen(
                modifier = modifierWithPaddingValues,
                theme = theme,
                dailyReminderEnabled = dailyReminderEnabled,
                reminderTime = reminderTime,
                onThemeChanged = viewModel::onThemeChange,
                onDailyReminderEnabledChanged = viewModel::onDailyReminderEnabledChange,
                onReminderTimeChanged = viewModel::onReminderTimeChange,
                onNavigateToManagePleasures = { navController.navigate(ManagePleasuresRoute) }
            )
        }

        composable<ManagePleasuresRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(
                        durationMillis = navigationAnimationDuration,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(
                        durationMillis = navigationAnimationDuration,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(
                        durationMillis = navigationAnimationDuration,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(
                        durationMillis = navigationAnimationDuration,
                        easing = LinearOutSlowInEasing
                    )
                )
            }
        ) {
            ManagePleasuresScreen(navigateBack = navController::popBackStack)
        }
    }
}
