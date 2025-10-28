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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private enum class ItemStatus {
    COMPLETED,
    MISSED,
    IN_PROGRESS
}

@Composable
private fun getItemStatus(item: PleasureHistoryEntry): ItemStatus {
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
private fun getDayText(dateDrawn: Long): String {
    val calendar = Calendar.getInstance()
    val today = calendar.clone() as Calendar

    calendar.timeInMillis = dateDrawn

    val isToday = today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)

    if (isToday) {
        return "Aujourd'hui"
    }

    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)
    val isYesterday = yesterday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
            yesterday.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)

    if (isYesterday) {
        return "Hier"
    }

    val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return dateFormat.format(Date(dateDrawn)).replaceFirstChar { it.uppercase() }
}

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
                animationDelay = index * 50
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

    val status = getItemStatus(item)
    val day = getDayText(item.dateDrawn)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable { onClick() }
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
                        text = day,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    StatusBadge(status = status)
                }

                Text(
                    text = item.pleasureTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
    }

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

@LightDarkPreview
@Composable
private fun WeeklyPleasuresListPreview() {
    DailyJoyTheme {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val previewItems = listOf(
                    PleasureHistoryEntry(
                        id = 1,
                        dayIdentifier = "",
                        dateDrawn = System.currentTimeMillis() - 86400000 * 2,
                        isCompleted = true,
                        pleasureTitle = "Aller au cinéma voir un bon film",
                        pleasureDescription = "Et si on se faisait un bon film ?",
                        category = PleasureCategory.ENTERTAINMENT
                    ),
                    PleasureHistoryEntry(
                        id = 2,
                        dayIdentifier = "",
                        dateDrawn = System.currentTimeMillis() - 86400000,
                        isCompleted = false,
                        pleasureTitle = "Faire une bouffe XXL entre amis",
                        pleasureDescription = "Ce soir, on mange sans regrêt",
                        category = PleasureCategory.FOOD
                    ),
                    PleasureHistoryEntry(
                        id = 3,
                        dayIdentifier = "",
                        dateDrawn = System.currentTimeMillis(),
                        isCompleted = false,
                        pleasureTitle = "Promenade dans le parc",
                        pleasureDescription = "Profiter de la nature",
                        category = PleasureCategory.OUTDOOR
                    ),
                    PleasureHistoryEntry(
                        id = 4,
                        dayIdentifier = "",
                        dateDrawn = System.currentTimeMillis() - 86400000 * 3,
                        isCompleted = true,
                        pleasureTitle = "Méditation et yoga",
                        pleasureDescription = "Moment de détente",
                        category = PleasureCategory.WELLNESS
                    )
                )
                WeeklyPleasuresList(
                    items = previewItems,
                    onCardClicked = {}
                )
            }
        }
    }
}
