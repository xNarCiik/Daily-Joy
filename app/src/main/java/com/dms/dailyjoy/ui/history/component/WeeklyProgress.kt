package com.dms.dailyjoy.ui.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun WeeklyProgress(completedCount: Int) {
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.surfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        (1..7).forEach { day ->
            val isCompleted = day <= completedCount

            if (day != 1) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1.0f)
                        .height(2.dp)
                        .padding(horizontal = 3.dp),
                    color = if (isCompleted) activeColor else inactiveColor
                )
            }

            ProgressItem(
                isCompleted = isCompleted,
                dayIndex = day,
                activeColor = activeColor,
                inactiveColor = inactiveColor
            )
        }
    }
}

@Composable
private fun ProgressItem(
    isCompleted: Boolean,
    dayIndex: Int,
    activeColor: Color,
    inactiveColor: Color
) {
    Surface(
        modifier = Modifier.size(32.dp),
        shape = CircleShape,
        color = if (isCompleted) activeColor else inactiveColor,
        contentColor = if (isCompleted) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isCompleted) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            } else {
                Text(text = dayIndex.toString(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@LightDarkPreview
@Composable
private fun WeeklyProgressPreview() {
    DailyJoyTheme {
        WeeklyProgress(completedCount = 3)
    }
}
