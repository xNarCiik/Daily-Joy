package com.dms.dailyjoy.ui.dailypleasure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.ui.navigation.DailyPleasureRoute

@Composable
fun DailyPleasureNavigation() {
    val navController = rememberNavController()
    val viewModel: PleasureViewModel = hiltViewModel()
    val dailyPleasureState by viewModel.state.collectAsState()

    NavHost(navController = navController, startDestination = DailyPleasureRoute) {
        composable<DailyPleasureRoute> {
            DailyPleasureScreen(
                dailyPleasureState = dailyPleasureState,
                onCardFlipped = viewModel::onDailyCardFlipped,
                onDonePleasure = viewModel::markDailyCardAsDone
            )
        }
    }
}
