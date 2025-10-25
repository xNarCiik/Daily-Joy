package com.dms.dailyjoy.ui.weekly.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import com.dms.dailyjoy.ui.social.PleasureStatus
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewWeeklyUiState
import kotlinx.coroutines.delay
import kotlin.collections.forEachIndexed

@Composable
fun WeeklyPleasuresList(
    modifier: Modifier = Modifier,
    items: List<PleasureHistoryEntry>,
    onCardClicked: (PleasureHistoryEntry) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items.forEachIndexed { index, item ->
            AnimatedPleasureItem(
                item = item,
                onClick = { onCardClicked(item) },
                animationDelay = index * 60
            )
        }
    }
}

@Composable
private fun AnimatedPleasureItem(
    item: PleasureHistoryEntry,
    onClick: () -> Unit,
    animationDelay: Int = 0
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(animationDelay.toLong())
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "alpha"
    )

    val isLocked = item.status == PleasureStatus.LOCKED
    val day = stringResource(id = item.dayNameRes)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable(enabled = !isLocked) { onClick() }
            .padding(vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            StatusIcon(status = item.status)

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (!isLocked) {
                        StatusBadge(status = item.status)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Titre du plaisir (en italique pour cohérence)
                item.pleasure?.let { pleasure ->
                    Text(
                        text = if (isLocked) stringResource(R.string.label_to_discover) else pleasure.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontStyle = if (!isLocked) FontStyle.Italic else FontStyle.Normal
                        ),
                        fontWeight = FontWeight.Medium,
                        color = if (isLocked) {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Divider
        Spacer(modifier = Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 62.dp)
                .height(0.5.dp)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
        )
    }
}

@Composable
private fun StatusIcon(
    status: PleasureStatus,
    modifier: Modifier = Modifier
) {
    val (icon, tint, backgroundColor) = when (status) {
        PleasureStatus.LOCKED -> Triple(
            Icons.Default.Lock,
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.surfaceVariant
        )

        PleasureStatus.PAST_COMPLETED, PleasureStatus.CURRENT_COMPLETED -> Triple(
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )

        PleasureStatus.PAST_NOT_COMPLETED -> Triple(
            Icons.Default.HighlightOff,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )

        else -> Triple(
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (status == PleasureStatus.CURRENT_REVEALED) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = tint
            )
        } else {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
        }
    }
}

@Composable
private fun StatusBadge(
    status: PleasureStatus,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (status) {
        PleasureStatus.PAST_COMPLETED, PleasureStatus.CURRENT_COMPLETED ->
            stringResource(R.string.status_done_checked) to MaterialTheme.colorScheme.primary

        PleasureStatus.PAST_NOT_COMPLETED ->
            stringResource(R.string.status_missed) to MaterialTheme.colorScheme.error

        PleasureStatus.CURRENT_REVEALED ->
            stringResource(R.string.status_in_progress) to MaterialTheme.colorScheme.secondary

        else -> return
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@LightDarkPreview
@Composable
private fun WeeklyPleasuresListPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            WeeklyPleasuresList(
                items = previewWeeklyUiState.history,
                onCardClicked = {}
            )
        }
    }
}