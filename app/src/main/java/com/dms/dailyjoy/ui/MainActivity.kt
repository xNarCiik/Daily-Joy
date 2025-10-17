@file:OptIn(ExperimentalMaterial3Api::class)

package com.dms.dailyjoy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.component.AnimatedBottomNavBar
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureScreen
import com.dms.dailyjoy.ui.history.HistoryScreen
import com.dms.dailyjoy.ui.settings.SettingsScreen
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.fadeInContentAnimationDuration
import com.dms.dailyjoy.ui.util.navigationAnimationDuration
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
            var contentVisible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                contentVisible = true
            }

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = fadeInContentAnimationDuration))
            ) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        imageVector = Icons.Default.Mood,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(Modifier.width(8.dp))

                                    Text(text = stringResource(R.string.app_name))
                                }
                            }
                        )
                    },
                    bottomBar = {
                        AnimatedBottomNavBar(
                            navController = navController,
                            durationAnimation = fadeInContentAnimationDuration
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = DailyPleasureRoute
                        ) {
                            composable<DailyPleasureRoute>(
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(durationMillis = navigationAnimationDuration)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(durationMillis = navigationAnimationDuration)
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
                                        animationSpec = tween(durationMillis = navigationAnimationDuration)
                                    )
                                },
                                exitTransition = {
                                    val rightTransition =
                                        targetState.destination.route == DailyPleasureRoute::class.qualifiedName
                                    slideOutOfContainer(
                                        towards = if (rightTransition) AnimatedContentTransitionScope.SlideDirection.Right else AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(durationMillis = navigationAnimationDuration)
                                    )
                                }
                            ) {
                                HistoryScreen()
                            }

                            composable<SettingsRoute>(
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(durationMillis = navigationAnimationDuration)
                                    )
                                },
                                exitTransition = {
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                        animationSpec = tween(durationMillis = navigationAnimationDuration)
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
}