package com.dms.dailyjoy.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.navigation.DailyPleasureRoute
import com.dms.dailyjoy.ui.navigation.HistoryRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(currentRoute: String?) {
    when (currentRoute) {
        DailyPleasureRoute::class.qualifiedName -> {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Default.Mood,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(Modifier.width(8.dp))

                        Text(text = stringResource(R.string.app_name))
                    }
                }
            )
        }

        HistoryRoute::class.qualifiedName -> {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.bottom_nav_bar_history_title)) }
            )
        }
    }
}