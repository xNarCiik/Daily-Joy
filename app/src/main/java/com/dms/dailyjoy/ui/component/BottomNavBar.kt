package com.dms.dailyjoy.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.navigation.DailyPleasureRoute
import com.dms.dailyjoy.ui.navigation.HistoryRoute
import com.dms.dailyjoy.ui.navigation.SettingsRoute
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

data class TabBarItem(
    val title: String,
    val icon: ImageVector,
    val badgeAmount: Int? = null,
    val route: Any
)

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeItem = TabBarItem(
        title = stringResource(R.string.daily_pleasure_title),
        icon = Icons.Filled.Mood,
        route = DailyPleasureRoute
    )
    val historyItem =
        TabBarItem(
            title = stringResource(R.string.history_title),
            icon = Icons.Filled.DateRange,
            route = HistoryRoute
        )
    val settingsItem =
        TabBarItem(
            title = stringResource(R.string.settings_title),
            icon = Icons.Filled.Settings,
            route = SettingsRoute
        )

    val tabBarItems = listOf(homeItem, historyItem, settingsItem)

    NavigationBar(tonalElevation = 4.dp) {
        tabBarItems.forEach { tabBarItem ->
            val isSelected = currentDestination?.route == tabBarItem.route::class.qualifiedName
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(tabBarItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = tabBarItem.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = tabBarItem.title)
                }
            )
        }
    }
}

@LightDarkPreview
@Composable
fun BottomNavBarPreview() {
    DailyJoyTheme {
        BottomNavBar(navController = rememberNavController())
    }
}