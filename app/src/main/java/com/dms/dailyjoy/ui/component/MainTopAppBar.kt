package com.dms.dailyjoy.ui.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.navigation.DailyPleasureRoute
import com.dms.dailyjoy.ui.navigation.HistoryRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(currentRoute: String?) {
    when (currentRoute) {
        DailyPleasureRoute::class.qualifiedName -> {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.daily_pleasure_title)) }
            )
        }

        HistoryRoute::class.qualifiedName -> {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.history_title)) }
            )
        }
    }
}