package com.dms.dailyjoy.ui.navigation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureViewModel
import com.dms.dailyjoy.ui.settings.SettingsScreen
import com.dms.dailyjoy.ui.settings.SettingsViewModel
import com.dms.dailyjoy.ui.settings.manage.ManagePleasuresScreen
import com.dms.dailyjoy.ui.settings.manage.ManagePleasuresViewModel
import com.dms.dailyjoy.ui.settings.statistics.StatisticsScreen
import com.dms.dailyjoy.ui.settings.statistics.StatisticsViewModel
import com.dms.dailyjoy.ui.social.SocialScreen
import com.dms.dailyjoy.ui.social.SocialViewModel
import com.dms.dailyjoy.ui.util.navigationAnimationDuration
import com.dms.dailyjoy.ui.weekly.Weekly
import com.dms.dailyjoy.ui.weekly.WeeklyScreen
import kotlinx.serialization.Serializable

@Serializable
object DailyPleasureRoute

@Serializable
object WeeklyRoute

@Serializable
object SocialRoute

@Serializable
object SettingsRoute

@Serializable
object ManagePleasuresRoute

@Serializable
object StatisticsRoute

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    val modifierWithPaddingValues = Modifier.padding(paddingValues)
    NavHost(
        navController = navController,
        startDestination = DailyPleasureRoute
    ) {
        composable<DailyPleasureRoute> {
            val viewModel: DailyPleasureViewModel = hiltViewModel()
            val dailyPleasureUiState by viewModel.uiState.collectAsState()

            DailyPleasureScreen(
                modifier = modifierWithPaddingValues,
                uiState = dailyPleasureUiState,
                onEvent = viewModel::onEvent
            )
        }

        composable<WeeklyRoute> {
            val viewModel: Weekly = hiltViewModel()
            val historyState by viewModel.uiState.collectAsState()

            WeeklyScreen(
                modifier = modifierWithPaddingValues,
                uiState = historyState,
                onEvent = viewModel::onEvent
            )
        }

        composable<SocialRoute> {
            val viewModel: SocialViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            SocialScreen(
                modifier = modifierWithPaddingValues,
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
        }

        composable<SettingsRoute> {
            val viewModel: SettingsViewModel = hiltViewModel()
            val settingsState by viewModel.uiState.collectAsState()

            SettingsScreen(
                modifier = modifierWithPaddingValues,
                uiState = settingsState,
                onEvent = viewModel::onEvent,
                onNavigateToManagePleasures = { navController.navigate(ManagePleasuresRoute) },
                onNavigateToStatistics = { navController.navigate(StatisticsRoute) }
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
            val viewModel: ManagePleasuresViewModel = hiltViewModel()
            val managePleasuresUiState by viewModel.uiState.collectAsState()

            ManagePleasuresScreen(
                uiState = managePleasuresUiState,
                onEvent = viewModel::onEvent,
                navigateBack = navController::popBackStack
            )
        }

        composable<StatisticsRoute> {
            val viewModel: StatisticsViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            StatisticsScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onNavigateBack = navController::popBackStack
            )
        }
    }
}
