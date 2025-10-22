package com.dms.dailyjoy.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.ui.component.AnimatedBottomNavBar
import com.dms.dailyjoy.ui.component.MainTopAppBar
import com.dms.dailyjoy.ui.navigation.DailyPleasureRoute
import com.dms.dailyjoy.ui.navigation.HistoryRoute
import com.dms.dailyjoy.ui.navigation.NavGraph
import com.dms.dailyjoy.ui.navigation.SettingsRoute
import com.dms.dailyjoy.ui.navigation.SocialRoute
import com.dms.dailyjoy.ui.settings.SettingsViewModel
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.fadeInContentAnimationDuration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !viewModel.isReady.value
        }

        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val settingsUiState by settingsViewModel.uiState.collectAsState()
            val useDarkTheme = when (settingsUiState.theme) {
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
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MainTopAppBar(currentRoute = currentRoute)
                    },
                    bottomBar = {
                        val visible = currentRoute in listOf(
                            DailyPleasureRoute::class.qualifiedName,
                            HistoryRoute::class.qualifiedName,
                            SocialRoute::class.qualifiedName,
                            SettingsRoute::class.qualifiedName
                        )

                        AnimatedBottomNavBar(
                            navController = navController,
                            visible = visible
                        )
                    }
                ) { innerPadding ->
                    NavGraph(navController = navController, paddingValues = innerPadding)
                }
            }
        }
    }
}