@file:OptIn(ExperimentalMaterial3Api::class)

package com.dms.dailyjoy.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.ui.component.AnimatedBottomNavBar
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureNavigation
import com.dms.dailyjoy.ui.history.HistoryNavigation
import com.dms.dailyjoy.ui.settings.SettingsNavigation
import com.dms.dailyjoy.ui.settings.SettingsViewModel
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.fadeInContentAnimationDuration
import com.dms.dailyjoy.ui.util.navigationAnimationDuration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val theme by settingsViewModel.theme.collectAsState()
            val useDarkTheme = when (theme) {
                Theme.LIGHT -> false
                Theme.DARK -> true
                Theme.SYSTEM -> isSystemInDarkTheme()
            }
            MainActivityContent(useDarkTheme = useDarkTheme)
        }
    }
}

@Composable
fun MainActivityContent(useDarkTheme: Boolean) {
    DailyJoyTheme(darkTheme = useDarkTheme) {
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
                val pagerState = rememberPagerState { 3 }

                var pagerEnabled by remember { mutableStateOf(true) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedBottomNavBar(
                            pagerState = pagerState,
                            durationAnimation = fadeInContentAnimationDuration
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(paddingValues = innerPadding)) {
                        val flingBehavior = PagerDefaults.flingBehavior(
                            state = pagerState,
                            snapAnimationSpec = tween(
                                durationMillis = navigationAnimationDuration,
                                easing = LinearOutSlowInEasing
                            )
                        )

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                            flingBehavior = flingBehavior,
                            userScrollEnabled = pagerEnabled
                        ) { page ->
                            when (page) {
                                0 -> DailyPleasureNavigation(
                                    onDraggingCard = { dragging -> pagerEnabled = !dragging }
                                )

                                1 -> HistoryNavigation()
                                2 -> SettingsNavigation()
                            }
                        }
                    }
                }
            }
        }
    }
}
