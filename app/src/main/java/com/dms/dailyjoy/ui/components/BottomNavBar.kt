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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
    val homeItem = TabBarItem(title = "Home", icon = Icons.Filled.Home, route = HomeRoute)
    val historyItem =
        TabBarItem(title = "History", icon = Icons.Filled.DateRange, route = HistoryRoute)
    val settingsItem =
        TabBarItem(title = "Settings", icon = Icons.Filled.Settings, route = SettingsRoute)

    val tabBarItems = listOf(homeItem, historyItem, settingsItem)

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            val isSelected = selectedTabIndex == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(route = tabBarItem.route)
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