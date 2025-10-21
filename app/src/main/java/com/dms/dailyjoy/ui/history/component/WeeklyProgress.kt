package com.dms.dailyjoy.ui.history.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import kotlinx.coroutines.delay

// TODO Remove ?
@Composable
fun WeeklyProgress(
    completedCount: Int,
    modifier: Modifier = Modifier
) {
    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            (1..7).forEach { day ->
                val isCompleted = day <= completedCount

                if (day != 1) {
                    ConnectorLine(
                        isCompleted = isCompleted,
                        activeColor = activeColor,
                        inactiveColor = inactiveColor,
                        modifier = Modifier.weight(1f)
                    )
                }

                ProgressItem(
                    isCompleted = isCompleted,
                    dayIndex = day,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    animationDelay = (day - 1) * 100
                )
            }
        }
    }
}

@Composable
private fun ConnectorLine(
    isCompleted: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    modifier: Modifier = Modifier
) {
    var shouldAnimate by remember { mutableStateOf(false) }

    LaunchedEffect(isCompleted) {
        shouldAnimate = isCompleted
    }

    val animatedWidth by animateFloatAsState(
        targetValue = if (shouldAnimate) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "line_width"
    )

    Box(modifier = modifier.padding(horizontal = 2.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(CircleShape)
                .background(inactiveColor)
        )

        if (isCompleted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedWidth)
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(activeColor)
            )
        }
    }
}

@Composable
private fun ProgressItem(
    isCompleted: Boolean,
    dayIndex: Int,
    activeColor: Color,
    inactiveColor: Color,
    animationDelay: Int = 0
) {
    var shouldAnimate by remember { mutableStateOf(false) }

    LaunchedEffect(isCompleted) {
        delay(animationDelay.toLong())
        shouldAnimate = isCompleted
    }

    val scale by animateFloatAsState(
        targetValue = if (shouldAnimate) 1f else 0.8f,
        animationSpec = tween(durationMillis = 300),
        label = "scale"
    )

    Surface(
        modifier = Modifier
            .size(36.dp)
            .scale(scale),
        shape = CircleShape,
        color = if (isCompleted) activeColor else inactiveColor,
        contentColor = if (isCompleted) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        shadowElevation = if (isCompleted) 4.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isCompleted) {
                Icon(
                    imageVector = if (dayIndex == 7) Icons.Default.EmojiEvents else Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = dayIndex.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun WeeklyProgressEmptyPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            WeeklyProgress(completedCount = 0)
        }
    }
}

@LightDarkPreview
@Composable
private fun WeeklyProgressPartialPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            WeeklyProgress(completedCount = 3)
        }
    }
}

@LightDarkPreview
@Composable
private fun WeeklyProgressFullPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            WeeklyProgress(completedCount = 7)
        }
    }
}
