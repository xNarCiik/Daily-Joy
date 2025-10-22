package com.dms.dailyjoy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null,
    val route: Any
)

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeItem = TabBarItem(
        title = stringResource(R.string.daily_pleasure_title),
        selectedIcon = Icons.Filled.Mood,
        unselectedIcon = Icons.Outlined.Mood,
        route = DailyPleasureRoute
    )
    val historyItem = TabBarItem(
        title = stringResource(R.string.history_title),
        selectedIcon = Icons.Filled.DateRange,
        unselectedIcon = Icons.Outlined.DateRange,
        route = HistoryRoute
    )
    val settingsItem = TabBarItem(
        title = stringResource(R.string.settings_title),
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = SettingsRoute
    )

    val tabBarItems = listOf(homeItem, historyItem, settingsItem)

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        tabBarItems.forEach { tabBarItem ->
            val isSelected = currentDestination?.route == tabBarItem.route::class.qualifiedName

            this@NavigationBar.NavigationBarItem(
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
                    Box(contentAlignment = Alignment.Center) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 0.3f
                                        )
                                    )
                            )
                        }

                        Icon(
                            imageVector = if (isSelected) tabBarItem.selectedIcon else tabBarItem.unselectedIcon,
                            contentDescription = tabBarItem.title,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                label = {
                    if (isSelected) {
                        Text(
                            text = tabBarItem.title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

@LightDarkPreview
@Composable
fun BottomNavBarPreview() {
    DailyJoyTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            BottomNavBar(navController = rememberNavController())
        }
    }
}
