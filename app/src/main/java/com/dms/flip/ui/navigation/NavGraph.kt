package com.dms.flip.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dms.flip.domain.model.RootNavigationState
import com.dms.flip.ui.dailypleasure.DailyPleasureScreen
import com.dms.flip.ui.dailypleasure.DailyPleasureViewModel
import com.dms.flip.ui.login.LoginScreen
import com.dms.flip.ui.onboarding.OnboardingScreen
import com.dms.flip.ui.settings.SettingsScreen
import com.dms.flip.ui.settings.SettingsViewModel
import com.dms.flip.ui.settings.manage.ManagePleasuresScreen
import com.dms.flip.ui.settings.manage.ManagePleasuresViewModel
import com.dms.flip.ui.settings.statistics.StatisticsScreen
import com.dms.flip.ui.settings.statistics.StatisticsViewModel
import com.dms.flip.ui.social.SocialScreen
import com.dms.flip.ui.social.SocialViewModel
import com.dms.flip.ui.weekly.WeeklyScreen
import com.dms.flip.ui.weekly.WeeklyViewModel
import kotlinx.serialization.Serializable

@Serializable
object RootRoute

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

private const val navigationAnimationDuration = 900

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    rootNavigationState: RootNavigationState
) {
    val modifierWithPaddingValues = Modifier.padding(paddingValues)

    val navigateSingleTop: (Any) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    val enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?) =
        {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 250,
                    easing = LinearOutSlowInEasing
                )
            )
        }

    val exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?) =
        {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 150,
                    easing = LinearOutSlowInEasing
                )
            )
        }

    val popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards EnterTransition?) =
        {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 250,
                    easing = LinearOutSlowInEasing
                )
            )
        }

    val popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> @JvmSuppressWildcards ExitTransition?) =
        {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 150,
                    easing = LinearOutSlowInEasing
                )
            )
        }

    // TODO CUSTOM TRANSITION
    NavHost(
        navController = navController,
        startDestination = RootRoute,
        // enterTransition = enterTransition
        /* enterTransition = enterTransition,
         exitTransition = exitTransition,
         popEnterTransition = popEnterTransition,
         popExitTransition = popExitTransition */
    ) {
        composable<RootRoute> {
            when (rootNavigationState) {
                RootNavigationState.NotAuthenticated -> {
                    LoginScreen()
                }

                RootNavigationState.AuthenticatedButNotOnboarded -> {
                    OnboardingScreen(modifier = modifierWithPaddingValues)
                }

                RootNavigationState.AuthenticatedAndOnboarded -> {
                    LaunchedEffect(Unit) {
                        navigateSingleTop(DailyPleasureRoute)
                    }
                }

                else -> {}
            }
        }

        composable<DailyPleasureRoute>(
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition
        ) {
            val viewModel: DailyPleasureViewModel = hiltViewModel()
            val dailyPleasureUiState by viewModel.uiState.collectAsState()

            DailyPleasureScreen(
                modifier = modifierWithPaddingValues,
                uiState = dailyPleasureUiState,
                onEvent = viewModel::onEvent,
                navigateToManagePleasures = { navController.navigate(ManagePleasuresRoute) }
            )
        }

        composable<WeeklyRoute> {
            val viewModel: WeeklyViewModel = hiltViewModel()
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
                onNavigateToStatistics = { navController.navigate(StatisticsRoute) },
                onSignOut = { navigateSingleTop(RootRoute) }
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
