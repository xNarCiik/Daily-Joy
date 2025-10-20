package com.dms.dailyjoy.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

enum class DailyPleasureStatus {
    PAST,
    CURRENT,
    FUTURE
}

data class WeeklyPleasure(
    val dayName: String,
    val pleasureTitle: String?,
    val status: DailyPleasureStatus,
    val isCompleted: Boolean = false
)

@Composable
fun HistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // État mutable pour le plaisir du jour actuel, pour simuler la validation
        var isTodayPleasureCompleted by remember { mutableStateOf(false) }

        // Liste placeholder pour la vue de la semaine (ex: aujourd'hui c'est Jeudi)
        val weeklyPleasures = remember(isTodayPleasureCompleted) {
            listOf(
                WeeklyPleasure(
                    "Lundi",
                    "Écouter le chant des oiseaux",
                    DailyPleasureStatus.PAST,
                    isCompleted = true
                ),
                WeeklyPleasure(
                    "Mardi",
                    "Boire un café chaud le matin",
                    DailyPleasureStatus.PAST,
                    isCompleted = false
                ),
                WeeklyPleasure(
                    "Mercredi",
                    "Faire une courte promenade",
                    DailyPleasureStatus.PAST,
                    isCompleted = true
                ),
                WeeklyPleasure(
                    "Aujourd'hui",
                    "Lire un chapitre d'un livre",
                    DailyPleasureStatus.CURRENT,
                    isCompleted = isTodayPleasureCompleted
                ),
                WeeklyPleasure("Vendredi", null, DailyPleasureStatus.FUTURE),
                WeeklyPleasure("Samedi", null, DailyPleasureStatus.FUTURE),
                WeeklyPleasure("Dimanche", null, DailyPleasureStatus.FUTURE)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(weeklyPleasures) { pleasure ->
                DailyPleasureCard(
                    item = pleasure,
                    onCompleteToday = {
                        // Ne permet de modifier que le plaisir du jour
                        if (pleasure.status == DailyPleasureStatus.CURRENT) {
                            isTodayPleasureCompleted = !isTodayPleasureCompleted
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun DailyPleasureCard(
    item: WeeklyPleasure,
    onCompleteToday: () -> Unit
) {
    when (item.status) {
        DailyPleasureStatus.FUTURE -> {
            // Carte "mystère" pour les jours à venir
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.dayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.QuestionMark,
                        contentDescription = "Plaisir à venir",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        DailyPleasureStatus.PAST, DailyPleasureStatus.CURRENT -> {
            val isPast = item.status == DailyPleasureStatus.PAST
            val cardAlpha = if (isPast) 0.7f else 1f

            Card(
                onClick = onCompleteToday,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(cardAlpha),
                enabled = !isPast // La carte n'est cliquable que pour le jour actuel
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.dayName,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.pleasureTitle ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (item.isCompleted) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Plaisir complété",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.RadioButtonUnchecked,
                            contentDescription = "Plaisir non complété",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}


@LightDarkPreview
@Composable
private fun HistoryScreenPreview() {
    DailyJoyTheme {
        HistoryScreen()
    }
}
