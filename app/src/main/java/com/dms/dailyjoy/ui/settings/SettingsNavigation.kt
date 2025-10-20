package com.dms.dailyjoy.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.ui.navigation.SettingsRoute
import com.dms.dailyjoy.ui.settings.manage.ManagePleasuresScreen
import kotlinx.serialization.Serializable

@Serializable
object ManagePleasuresRoute

@Composable
fun SettingsNavigation() {
    val navController = rememberNavController()
    val viewModel: SettingsViewModel = hiltViewModel()
    val theme by viewModel.theme.collectAsState()

    NavHost(navController = navController, startDestination = SettingsRoute) {
        composable<SettingsRoute> {
            SettingsScreen(
                theme = theme,
                onThemeChanged = viewModel::onThemeChange,
                onNavigateToManagePleasures = { navController.navigate(ManagePleasuresRoute) }
            )
        }
        composable<ManagePleasuresRoute> {
            ManagePleasuresScreen(navigateBack = navController::popBackStack)
        }
    }
}
