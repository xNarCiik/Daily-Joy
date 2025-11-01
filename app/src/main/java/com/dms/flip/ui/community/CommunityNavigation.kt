package com.dms.flip.ui.community

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dms.flip.ui.community.screen.*

sealed class CommunityRoute(val route: String) {
    data object Main : CommunityRoute("community_main")
    data object Search : CommunityRoute("community_search")
    data object Profile : CommunityRoute("community_profile/{userId}") {
        fun createRoute(userId: String) = "community_profile/$userId"
    }
}

@Composable
fun CommunityNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CommunityRoute.Main.route,
        modifier = modifier
    ) {
        composable(CommunityRoute.Main.route) { backStackEntry ->
            val viewModel: CommunityViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            CommunityScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is CommunityEvent.OnSearchClicked -> {
                            navController.navigate(CommunityRoute.Search.route)
                        }
                        is CommunityEvent.OnFriendClicked -> {
                            navController.navigate(
                                CommunityRoute.Profile.createRoute(event.friend.id)
                            )
                        }
                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }

        composable(CommunityRoute.Search.route) { backStackEntry ->
            val viewModel: CommunityViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            SearchFriendsScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is CommunityEvent.OnSearchResultClicked -> {
                            navController.navigate(
                                CommunityRoute.Profile.createRoute(event.result.id)
                            )
                        }
                        else -> viewModel.onEvent(event)
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = CommunityRoute.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: CommunityViewModel = hiltViewModel(backStackEntry)
            val userId = backStackEntry.arguments?.getString("userId")

            // Idéalement, expose un flow/state dans le VM, mais on garde ta logique actuelle :
            userId?.let {
                val profile = viewModel.getPublicProfile(it)
                if (profile != null) {
                    PublicProfileScreen(
                        profile = profile,
                        onAddFriend = {
                            viewModel.onEvent(CommunityEvent.OnAddUserFromSearch(it))
                            navController.navigateUp()
                        },
                        onNavigateBack = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}
