package com.dms.dailyjoy.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.HistoryRoute
import com.dms.dailyjoy.ui.HomeRoute
import com.dms.dailyjoy.ui.SettingsRoute
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

data class TabBarItem(
    val title: String,
    val icon: ImageVector,
    val badgeAmount: Int? = null,
    val route: Any
)

@Composable
fun BottomNavBar(navController: NavController) {
    val homeItem = TabBarItem(
        title = stringResource(R.string.bottom_nav_bar_home_title),
        icon = Icons.Filled.Home,
        route = HomeRoute
    )
    val historyItem =
        TabBarItem(
            title = stringResource(R.string.bottom_nav_bar_history_title),
            icon = Icons.Filled.DateRange,
            route = HistoryRoute
        )
    val settingsItem =
        TabBarItem(
            title = stringResource(R.string.bottom_nav_bar_settings_title),
            icon = Icons.Filled.Settings,
            route = SettingsRoute
        )

    val tabBarItems = listOf(homeItem, historyItem, settingsItem)

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            val isSelected = selectedTabIndex == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        selectedTabIndex = index
                        navController.navigate(route = tabBarItem.route) {
                            popUpTo(
                                route = navController.currentBackStackEntry?.destination?.route
                                    ?: ""
                            ) {
                                inclusive = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = tabBarItem.icon,
                        tint = if (isSelected) Color.Black else Color.Gray,
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

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    DailyJoyTheme {
        BottomNavBar(navController = rememberNavController())
    }
}