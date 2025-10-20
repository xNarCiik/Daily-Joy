package com.dms.dailyjoy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureNavigation
import com.dms.dailyjoy.ui.history.HistoryNavigation
import com.dms.dailyjoy.ui.settings.SettingsNavigation
import kotlinx.serialization.Serializable

@Serializable
object DailyPleasureRoute
@Serializable
object HistoryRoute
@Serializable
object SettingsRoute

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = DailyPleasureRoute
    ) {
        composable<DailyPleasureRoute>{
            DailyPleasureNavigation()
        }
        composable<HistoryRoute> {
            HistoryNavigation()
        }
        composable<SettingsRoute> {
            SettingsNavigation()
        }
    }
}
