package com.dms.flip.ui.history.component

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
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.HighlightOff
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dms.flip.R
import com.dms.flip.data.database.entity.PleasureHistoryEntry
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.history.WeeklyDay
import kotlinx.coroutines.delay
import java.util.Calendar

private enum class ItemStatus {
    COMPLETED,
    MISSED,
    IN_PROGRESS,
    EMPTY
}

@Composable
private fun getItemStatus(item: PleasureHistoryEntry?): ItemStatus {
    if (item == null) {
        return ItemStatus.EMPTY
    }

    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_YEAR)
    val year = calendar.get(Calendar.YEAR)

    calendar.timeInMillis = item.dateDrawn
    val itemDay = calendar.get(Calendar.DAY_OF_YEAR)
    val itemYear = calendar.get(Calendar.YEAR)

    val isToday = today == itemDay && year == itemYear

    return when {
        item.isCompleted -> ItemStatus.COMPLETED
        !isToday -> ItemStatus.MISSED
        else -> ItemStatus.IN_PROGRESS
    }
}

@Composable
fun WeeklyPleasuresList(
    modifier: Modifier = Modifier,
    items: List<WeeklyDay>,
    onCardClicked: (PleasureHistoryEntry) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items.forEachIndexed { index, weeklyDay ->
            AnimatedPleasureItem(
                weeklyDay = weeklyDay,
                onClick = { weeklyDay.historyEntry?.let(onCardClicked) },
                animationDelay = index * 50
            )
        }
    }
}

@Composable
private fun AnimatedPleasureItem(
    weeklyDay: WeeklyDay,
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

    val status = getItemStatus(weeklyDay.historyEntry)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable(enabled = weeklyDay.historyEntry != null) { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusIcon(status = status)

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = weeklyDay.dayName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (weeklyDay.historyEntry != null) {
                        StatusBadge(status = status)
                    }
                }

                Text(
                    text = weeklyDay.historyEntry?.pleasureTitle
                        ?: stringResource(R.string.social_no_pleasure_today),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (weeklyDay.historyEntry != null) FontWeight.Medium else FontWeight.Normal,
                    color = if (weeklyDay.historyEntry != null) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.outline,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Divider avec gradient
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun StatusIcon(
    status: ItemStatus,
    modifier: Modifier = Modifier
) {
    val (icon, tint, backgroundColor) = when (status) {
        ItemStatus.COMPLETED -> Triple(
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.primary,
            Brush.radialGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            )
        )

        ItemStatus.MISSED -> Triple(
            Icons.Default.HighlightOff,
            MaterialTheme.colorScheme.error,
            Brush.radialGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                )
            )
        )

        ItemStatus.IN_PROGRESS -> Triple(
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.secondary,
            Brush.radialGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                )
            )
        )

        ItemStatus.EMPTY -> Triple(
            Icons.Default.DoNotDisturb,
            MaterialTheme.colorScheme.outline,
            Brush.radialGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                )
            )
        )
    }

    Box(
        modifier = modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (status == ItemStatus.IN_PROGRESS) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.5.dp,
                color = tint
            )
        } else {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
        }
    }
}

@Composable
private fun StatusBadge(
    status: ItemStatus,
    modifier: Modifier = Modifier
) {
    val (textRes, color, backgroundColor) = when (status) {
        ItemStatus.COMPLETED -> Triple(
            R.string.status_done_checked,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        )

        ItemStatus.MISSED -> Triple(
            R.string.status_missed,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        )

        ItemStatus.IN_PROGRESS -> Triple(
            R.string.status_in_progress,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )

        ItemStatus.EMPTY -> Triple(null, Color.Transparent, Color.Transparent)
    }

    if (textRes != null) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun WeeklyPleasuresListPreview() {
    FlipTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val previewItems = listOf(
                    WeeklyDay(
                        dayName = "Lundi",
                        historyEntry = PleasureHistoryEntry(
                            id = 1,
                            dayIdentifier = "",
                            dateDrawn = System.currentTimeMillis() - 86400000 * 2,
                            isCompleted = true,
                            pleasureTitle = "Aller au cinéma voir un bon film",
                            pleasureDescription = "Et si on se faisait un bon film ?",
                            category = PleasureCategory.ENTERTAINMENT
                        )
                    ),
                    WeeklyDay(
                        dayName = "Mardi",
                        historyEntry = null
                    ),
                    WeeklyDay(
                        dayName = "Mercredi",
                        historyEntry = PleasureHistoryEntry(
                            id = 2,
                            dayIdentifier = "",
                            dateDrawn = System.currentTimeMillis() - 86400000,
                            isCompleted = false,
                            pleasureTitle = "Faire une bouffe XXL entre amis",
                            pleasureDescription = "Ce soir, on mange sans regrêt",
                            category = PleasureCategory.FOOD
                        )
                    ),
                    WeeklyDay(
                        dayName = "Jeudi",
                        historyEntry = PleasureHistoryEntry(
                            id = 3,
                            dayIdentifier = "",
                            dateDrawn = System.currentTimeMillis(),
                            isCompleted = false,
                            pleasureTitle = "Promenade dans le parc",
                            pleasureDescription = "Profiter de la nature",
                            category = PleasureCategory.OUTDOOR
                        )
                    )
                )
                WeeklyPleasuresList(items = previewItems, onCardClicked = { })
            }
        }
    }
}
