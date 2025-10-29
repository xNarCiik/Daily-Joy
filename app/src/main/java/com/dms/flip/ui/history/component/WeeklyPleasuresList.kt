package com.dms.flip.ui.history.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.dms.flip.ui.history.WeeklyDay
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import kotlinx.coroutines.delay
import java.util.Calendar

private enum class DayType {
    COMPLETED,    // Jour complété
    TODAY,        // Jour actuel (à découvrir)
    LOCKED        // Jour futur verrouillé
}

@Composable
private fun getDayType(item: PleasureHistoryEntry?): DayType {
    if (item == null) {
        return DayType.LOCKED
    }

    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_YEAR)
    val year = calendar.get(Calendar.YEAR)

    calendar.timeInMillis = item.dateDrawn
    val itemDay = calendar.get(Calendar.DAY_OF_YEAR)
    val itemYear = calendar.get(Calendar.YEAR)

    val isToday = today == itemDay && year == itemYear

    return when {
        item.isCompleted -> DayType.COMPLETED
        isToday -> DayType.TODAY
        else -> DayType.LOCKED
    }
}

@Composable
fun WeeklyPleasuresList(
    modifier: Modifier = Modifier,
    items: List<WeeklyDay>,
    onCardClicked: (PleasureHistoryEntry) -> Unit,
    onDiscoverTodayClicked: () -> Unit = {} // TODO: Lier au ViewModel
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEachIndexed { index, weeklyDay ->
            val dayType = getDayType(weeklyDay.historyEntry)

            AnimatedDayItem(
                weeklyDay = weeklyDay,
                dayType = dayType,
                onClick = { weeklyDay.historyEntry?.let(onCardClicked) },
                onDiscoverClick = onDiscoverTodayClicked,
                animationDelay = index * 50
            )
        }
    }
}

@Composable
private fun AnimatedDayItem(
    weeklyDay: WeeklyDay,
    dayType: DayType,
    onClick: () -> Unit,
    onDiscoverClick: () -> Unit,
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

    Box(modifier = Modifier.alpha(alpha)) {
        when (dayType) {
            DayType.COMPLETED -> CompletedDayCard(
                weeklyDay = weeklyDay,
                onClick = onClick
            )

            DayType.TODAY -> TodayCard(
                weeklyDay = weeklyDay,
                onDiscoverClick = onDiscoverClick
            )

            DayType.LOCKED -> LockedDayCard(
                dayName = weeklyDay.dayName
            )
        }
    }
}

// ========== COMPLETED DAY CARD ==========
@Composable
private fun CompletedDayCard(
    weeklyDay: WeeklyDay,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth()
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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Icône verte avec fond
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFA5D6A7)), // Success green from mockup
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Récupérer l'icône de la catégorie depuis weeklyDay.historyEntry?.category
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = weeklyDay.dayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = weeklyDay.historyEntry?.pleasureTitle ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Checkmark vert
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(R.string.status_done_checked),
                tint = Color(0xFFA5D6A7),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// ========== TODAY CARD (Grande Card avec Gradient) ==========
@Composable
private fun TodayCard(
    weeklyDay: WeeklyDay,
    onDiscoverClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        shadowElevation = 4.dp,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Height réduit pour être moins imposant
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFB39DDB), // Lavender
                                Color(0xFF90CAF9)  // Light blue
                            )
                        )
                    )
            )

            // Contenu textuel + bouton
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = weeklyDay.dayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.history_ready_for_joy),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onDiscoverClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.history_discover_button),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ========== LOCKED DAY CARD ==========
@Composable
private fun LockedDayCard(
    dayName: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.6f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icône calendrier grisée
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = dayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = stringResource(R.string.history_day_locked),
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ========== PREVIEWS ==========
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
                            pleasureTitle = "Savourer un café chaud",
                            pleasureDescription = "Prendre le temps de déguster",
                            category = PleasureCategory.FOOD
                        )
                    ),
                    WeeklyDay(
                        dayName = "Mardi",
                        historyEntry = PleasureHistoryEntry(
                            id = 2,
                            dayIdentifier = "",
                            dateDrawn = System.currentTimeMillis() - 86400000,
                            isCompleted = true,
                            pleasureTitle = "Lire quelques pages d'un livre",
                            pleasureDescription = "Se plonger dans une histoire",
                            category = PleasureCategory.LEARNING
                        )
                    ),
                    WeeklyDay(
                        dayName = "Mercredi",
                        historyEntry = PleasureHistoryEntry(
                            id = 3,
                            dayIdentifier = "",
                            dateDrawn = System.currentTimeMillis(),
                            isCompleted = false,
                            pleasureTitle = "Plaisir du jour",
                            pleasureDescription = "",
                            category = PleasureCategory.ALL
                        )
                    ),
                    WeeklyDay(dayName = "Jeudi", historyEntry = null),
                    WeeklyDay(dayName = "Vendredi", historyEntry = null),
                    WeeklyDay(dayName = "Samedi", historyEntry = null),
                    WeeklyDay(dayName = "Dimanche", historyEntry = null)
                )
                WeeklyPleasuresList(
                    items = previewItems,
                    onCardClicked = {},
                    onDiscoverTodayClicked = {}
                )
            }
        }
    }
}
