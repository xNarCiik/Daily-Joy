package com.dms.flip.ui.community

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dms.flip.ui.community.screen.*

sealed class CommunityRoute(val route: String) {
    data object Main : CommunityRoute("community_main")
    data object Invitations : CommunityRoute("community_invitations")
    data object FriendsList : CommunityRoute("community_friends_list")
    data object Search : CommunityRoute("community_search")
    data object Profile : CommunityRoute("community_profile/{userId}") {
        fun createRoute(userId: String) = "community_profile/$userId"
    }
}

@Composable
fun CommunityNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = CommunityRoute.Main.route,
        modifier = modifier
    ) {
        composable(CommunityRoute.Main.route) {
            CommunityScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is CommunityEvent.OnSearchClicked -> {
                            navController.navigate(CommunityRoute.Search.route)
                        }

                        is CommunityEvent.OnFriendsListClicked -> {
                            navController.navigate(CommunityRoute.FriendsList.route)
                        }

                        is CommunityEvent.OnInvitationsClicked -> {
                            navController.navigate(CommunityRoute.Invitations.route)
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

        composable(CommunityRoute.Invitations.route) {
            InvitationsScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is CommunityEvent.OnViewProfile -> {
                            navController.navigate(
                                CommunityRoute.Profile.createRoute(event.userId)
                            )
                        }

                        else -> viewModel.onEvent(event)
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(CommunityRoute.FriendsList.route) {
            FriendsListScreen(
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

        composable(CommunityRoute.Search.route) {
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
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")

            if (userId != null) {
                val profile = viewModel.getPublicProfile(userId)

                if (profile != null) {
                    PublicProfileScreen(
                        profile = profile,
                        onAddFriend = {
                            viewModel.onEvent(CommunityEvent.OnAddUserFromSearch(userId))
                            navController.navigateUp()
                        },
                        onNavigateBack = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}
