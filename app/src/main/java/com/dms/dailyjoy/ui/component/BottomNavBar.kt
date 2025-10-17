package com.dms.dailyjoy.ui.component

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.DailyPleasureRoute
import com.dms.dailyjoy.ui.HistoryRoute
import com.dms.dailyjoy.ui.SettingsRoute
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import kotlinx.coroutines.launch

data class TabBarItem(
    val title: String,
    val icon: ImageVector,
    val badgeAmount: Int? = null,
    val route: Any
)

@Composable
fun BottomNavBar(pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    val homeItem = TabBarItem(
        title = stringResource(R.string.bottom_nav_bar_daily_pleasure_title),
        icon = Icons.Filled.Mood,
        route = DailyPleasureRoute
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

    NavigationBar(tonalElevation = 4.dp) {
        tabBarItems.forEachIndexed { index, tabBarItem ->
            val isSelected = pagerState.currentPage == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
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
        BottomNavBar(pagerState = rememberPagerState { 3 })
    }
}