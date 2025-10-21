package com.dms.dailyjoy.ui.history.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.history.PleasureStatus
import com.dms.dailyjoy.ui.history.WeeklyPleasureItem
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewHistoryUiState
import kotlinx.coroutines.delay

@Composable
fun PleasuresList(
    modifier: Modifier = Modifier,
    items: List<WeeklyPleasureItem>,
    onCardClicked: (WeeklyPleasureItem) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEachIndexed { index, item ->
            AnimatedPleasureCard(
                item = item,
                onClick = { onCardClicked(item) },
                animationDelay = index * 80
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedPleasureCard(
    item: WeeklyPleasureItem,
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

    val cardColors = when (item.status) {
        PleasureStatus.LOCKED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )

        PleasureStatus.PAST_COMPLETED, PleasureStatus.CURRENT_COMPLETED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )

        PleasureStatus.PAST_NOT_COMPLETED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )

        else -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    }

    Card(
        onClick = { if (!isLocked) onClick() },
        enabled = !isLocked,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        colors = cardColors,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isLocked) 0.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatusIcon(status = item.status)

                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    item.pleasure?.let { pleasure ->
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isLocked) stringResource(R.string.label_to_discover) else pleasure.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isLocked) {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            if (!isLocked) {
                StatusBadge(status = item.status)
            }
        }
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
            MaterialTheme.colorScheme.primaryContainer
        )

        PleasureStatus.PAST_NOT_COMPLETED -> Triple(
            Icons.Default.HighlightOff,
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.errorContainer
        )

        else -> Triple(
            Icons.Default.CheckCircle,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondaryContainer
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
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                modifier = Modifier.size(24.dp),
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
            .padding(horizontal = 12.dp, vertical = 6.dp)
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
private fun PleasuresListPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            PleasuresList(
                items = previewHistoryUiState.weeklyPleasures,
                onCardClicked = {}
            )
        }
    }
}
