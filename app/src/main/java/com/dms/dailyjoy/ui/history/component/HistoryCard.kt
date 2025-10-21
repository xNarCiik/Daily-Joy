package com.dms.dailyjoy.ui.history.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
fun HistoryCard(
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
        modifier = modifier.height(140.dp),
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
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isLocked) Arrangement.Center else Arrangement.SpaceBetween
        ) {
            Text(
                text = day,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isLocked) {
                Spacer(Modifier.height(8.dp))
            }

            Box(
                modifier = Modifier.weight(1f, fill = !isLocked),
                contentAlignment = Alignment.Center
            ) {
                val iconModifier = Modifier.size(28.dp)
                when (item.status) {
                    PleasureStatus.LOCKED -> {
                        Icon(
                            modifier = iconModifier,
                            imageVector = Icons.Default.Lock,
                            contentDescription = stringResource(R.string.status_locked),
                            tint = contentColor
                        )
                    }

                    PleasureStatus.PAST_COMPLETED, PleasureStatus.CURRENT_COMPLETED -> {
                        Icon(
                            modifier = iconModifier,
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = stringResource(R.string.status_completed),
                            tint = contentColor
                        )
                    }

                    PleasureStatus.PAST_NOT_COMPLETED -> {
                        Icon(
                            modifier = iconModifier,
                            imageVector = Icons.Default.HighlightOff,
                            contentDescription = stringResource(R.string.status_not_completed),
                            tint = contentColor
                        )
                    }

                    else -> {
                        CircularProgressIndicator(modifier = iconModifier)
                    }
                }
            }

            if (item.pleasure != null && !isLocked) {
                Text(
                    text = item.pleasure.title,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    maxLines = 2,
                    minLines = 2
                )
            } else if (!isLocked) {
                Spacer(modifier = Modifier.height(30.dp)) // Reserve space to maintain card height
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun HistoryCardPreview() {
    DailyJoyTheme {
        HistoryCard(
            item = WeeklyPleasureItem(
                dayNameRes = R.string.full_day_monday,
                status = PleasureStatus.LOCKED,
                pleasure = previewDailyPleasure
            ),
            onClick = {}
        )
    }
}
