package com.dms.flip.ui.community

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dms.flip.ui.community.screen.CommunityScreen
import com.dms.flip.ui.community.screen.FriendRequestsScreen
import com.dms.flip.ui.community.screen.FriendsListScreen
import com.dms.flip.ui.community.screen.PublicProfileScreen
import com.dms.flip.ui.community.screen.SearchFriendsScreen
import com.dms.flip.ui.community.screen.SuggestionsScreen

/**
 * Routes pour la navigation Community
 */
sealed class CommunityRoute(val route: String) {
    data object Main : CommunityRoute("community_main")
    data object FriendsList : CommunityRoute("friends_list")
    data object Search : CommunityRoute("search")
    data object Suggestions : CommunityRoute("suggestions")
    data object FriendRequests : CommunityRoute("friend_requests")
    data object PublicProfile : CommunityRoute("public_profile/{userId}") {
        fun createRoute(userId: String) = "public_profile/$userId"
    }
}

/**
 * Navigation host pour la section Community
 * Gère tous les écrans et la navigation entre eux
 */
@Composable
fun CommunityNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: CommunityViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = CommunityRoute.Main.route,
        modifier = modifier
    ) {
        // Écran principal Community avec tabs
        composable(CommunityRoute.Main.route) {
            CommunityScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is CommunityEvent.OnSearchClicked -> {
                            navController.navigate(CommunityRoute.Search.route)
                        }
                        is CommunityEvent.OnAddFriendClicked -> {
                            navController.navigate(CommunityRoute.FriendRequests.route)
                        }
                        is CommunityEvent.OnFriendClicked -> {
                            navController.navigate(
                                CommunityRoute.PublicProfile.createRoute(event.friend.id)
                            )
                        }
                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }

        // Liste complète des amis
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
                                CommunityRoute.PublicProfile.createRoute(event.friend.id)
                            )
                        }
                        else -> viewModel.onEvent(event)
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Recherche d'amis
        composable(CommunityRoute.Search.route) {
            SearchFriendsScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is CommunityEvent.OnSearchResultClicked -> {
                            navController.navigate(
                                CommunityRoute.PublicProfile.createRoute(event.result.id)
                            )
                        }
                        else -> viewModel.onEvent(event)
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Suggestions d'amis
        composable(CommunityRoute.Suggestions.route) {
            SuggestionsScreen(
                uiState = uiState,
                onEvent = { event ->
                    when (event) {
                        is CommunityEvent.OnSearchClicked -> {
                            navController.navigate(CommunityRoute.Search.route)
                        }
                        else -> viewModel.onEvent(event)
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Demandes d'amis
        composable(CommunityRoute.FriendRequests.route) {
            FriendRequestsScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Profil public d'un utilisateur
        composable(CommunityRoute.PublicProfile.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable

            // Récupérer le profil depuis le ViewModel
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