package com.dms.dailyjoy.ui.history

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.ui.SettingsRoute

@Composable
fun HistoryNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SettingsRoute) {
        composable<SettingsRoute> {
            HistoryScreen(navController = navController)
        }
    }
}
