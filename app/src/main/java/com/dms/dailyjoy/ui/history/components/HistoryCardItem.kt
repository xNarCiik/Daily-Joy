package com.dms.dailyjoy.ui.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.history.PleasureStatus
import com.dms.dailyjoy.ui.history.WeeklyPleasureItem
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasure

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryCardItem(
    modifier: Modifier = Modifier,
    item: WeeklyPleasureItem,
    onClick: () -> Unit
) {
    val day = stringResource(id = item.dayNameRes)

    val cardColors = when (item.status) {
        PleasureStatus.LOCKED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )

        else -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }

    val contentColor = when (item.status) {
        PleasureStatus.PAST_COMPLETED, PleasureStatus.CURRENT_COMPLETED -> MaterialTheme.colorScheme.primary
        PleasureStatus.PAST_NOT_COMPLETED, PleasureStatus.CURRENT_REVEALED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    }

    val isLocked = item.status == PleasureStatus.LOCKED

    Card(
        modifier = modifier.padding(4.dp),
        onClick = { if (!isLocked) onClick() },
        enabled = !isLocked,
        shape = MaterialTheme.shapes.medium,
        colors = cardColors,
        border = if (isLocked) BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ) else null
    ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = day,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when (item.status) {
                    PleasureStatus.LOCKED -> {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.status_locked),
                            modifier = Modifier.size(28.dp),
                            tint = contentColor
                        )
                    }

                    PleasureStatus.PAST_COMPLETED, PleasureStatus.CURRENT_COMPLETED -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.status_completed),
                            modifier = Modifier.size(32.dp),
                            tint = contentColor
                        )
                    }

                    PleasureStatus.PAST_NOT_COMPLETED -> {
                        Icon(
                            imageVector = Icons.Default.HighlightOff,
                            contentDescription = stringResource(R.string.status_not_completed),
                            modifier = Modifier.size(32.dp),
                            tint = contentColor
                        )
                    }

                    else -> {}
                }
            }

            if (item.pleasure != null && !isLocked) {
                Text(
                    text = item.pleasure.title,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    minLines = 2
                )
            } else {
                // Espace réservé pour garder la même hauteur de carte
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun HistoryCardItemPreview() {
    DailyJoyTheme {
        HistoryCardItem(
            item = WeeklyPleasureItem(
                dayNameRes = R.string.day_monday,
                status = PleasureStatus.CURRENT_COMPLETED,
                pleasure = previewDailyPleasure
            ),
            onClick = {}
        )
    }
}
