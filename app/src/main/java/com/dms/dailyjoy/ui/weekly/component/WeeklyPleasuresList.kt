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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
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

    val status = getItemStatus(item)
    val day = getDayText(item.dateDrawn)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            StatusIcon(status = status)

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
                    StatusBadge(status = status)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.pleasureTitle,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
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
    status: ItemStatus,
    modifier: Modifier = Modifier
) {
    val (icon, tint, backgroundColor) = when (status) {
        ItemStatus.COMPLETED -> Triple(
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )

        ItemStatus.MISSED -> Triple(
            Icons.Default.HighlightOff,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )

        ItemStatus.IN_PROGRESS -> Triple(
            Icons.Default.CheckCircle, // Placeholder icon
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
        if (status == ItemStatus.IN_PROGRESS) {
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
    status: ItemStatus,
    modifier: Modifier = Modifier
) {
    val (textRes, color) = when (status) {
        ItemStatus.COMPLETED ->
            R.string.status_done_checked to MaterialTheme.colorScheme.primary

        ItemStatus.MISSED ->
            R.string.status_missed to MaterialTheme.colorScheme.error

        ItemStatus.IN_PROGRESS ->
            R.string.status_in_progress to MaterialTheme.colorScheme.secondary
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = stringResource(textRes),
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
            val previewItems = listOf(
                PleasureHistoryEntry(
                    id = 1,
                    dayIdentifier = "",
                    dateDrawn = System.currentTimeMillis() - 86400000 * 2, // 2 days ago
                    isCompleted = true,
                    pleasureTitle = "Aller au cinéma",
                    pleasureDescription = "Et si on se faisait un bon film ?",
                    category = PleasureCategory.ALL
                ),
                PleasureHistoryEntry(
                    id = 2,
                    dayIdentifier = "",
                    dateDrawn = System.currentTimeMillis() - 86400000, // Yesterday
                    isCompleted = false,
                    pleasureTitle = "Faire une bouffe XXL",
                    pleasureDescription = "Ce soir, on mange sans regrêt",
                    category = PleasureCategory.ALL
                ),
                PleasureHistoryEntry(
                    id = 3,
                    dayIdentifier = "",
                    dateDrawn = System.currentTimeMillis(),
                    isCompleted = false,
                    pleasureTitle = "Aller au cinéma",
                    pleasureDescription = "Et si on se faisait un bon film ?",
                    category = PleasureCategory.ALL
                )
            )
            WeeklyPleasuresList(
                items = previewItems,
                onCardClicked = {}
            )
        }
    }
}
