@file:OptIn(ExperimentalMaterial3Api::class)

package com.dms.dailyjoy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.component.BottomNavBar
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureScreen
import com.dms.dailyjoy.ui.history.HistoryScreen
import com.dms.dailyjoy.ui.settings.SettingsScreen
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainActivityContent()
        }
    }
}

@Composable
fun MainActivityContent() {
    val navController = rememberNavController()

    DailyJoyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
                },
                bottomBar = {
                    BottomNavBar(navController = navController)
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
                    NavHost(navController = navController, startDestination = DailyPleasureRoute) {
                        composable<DailyPleasureRoute>(
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(durationMillis = 700)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(durationMillis = 700)
                                )
                            }
                        ) {
                            DailyPleasureScreen()
                        }

                        composable<HistoryRoute>(
                            enterTransition = {
                                val leftTransition =
                                    initialState.destination.route == DailyPleasureRoute::class.qualifiedName
                                slideIntoContainer(
                                    towards = if (leftTransition) AnimatedContentTransitionScope.SlideDirection.Left else AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(durationMillis = 700)
                                )
                            },
                            exitTransition = {
                                val rightTransition =
                                    targetState.destination.route == DailyPleasureRoute::class.qualifiedName
                                slideOutOfContainer(
                                    towards = if (rightTransition) AnimatedContentTransitionScope.SlideDirection.Right else AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(durationMillis = 700)
                                )
                            }
                        ) {
                            HistoryScreen()
                        }

                        composable<SettingsRoute>(
                            enterTransition = {
                                slideIntoContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                    animationSpec = tween(durationMillis = 700)
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                    animationSpec = tween(durationMillis = 700)
                                )
                            }
                        ) {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityContentPreview() {
    DailyJoyTheme {
        MainActivityContent()
    }
}