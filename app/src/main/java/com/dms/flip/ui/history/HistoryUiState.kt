package com.dms.flip.ui.history

import com.dms.flip.data.database.entity.PleasureHistoryEntry
import java.util.Locale

data class HistoryUiState(
    val isLoading: Boolean = false,
    val weeklyDays: List<WeeklyDay> = emptyList(),
    val error: String? = null,
    val selectedHistoryEntry: PleasureHistoryEntry? = null
)

data class WeeklyDay(
    val dayName: String,
    val historyEntry: PleasureHistoryEntry?
)

sealed interface HistoryEvent {
    data object OnRetryClicked : HistoryEvent
    data class OnCardClicked(val item: PleasureHistoryEntry) : HistoryEvent
    data object OnBottomSheetDismissed : HistoryEvent
}

fun getDayName(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        1 -> "Lundi"
        2 -> "Mardi"
        3 -> "Mercredi"
        4 -> "Jeudi"
        5 -> "Vendredi"
        6 -> "Samedi"
        7 -> "Dimanche"
        else -> ""
    }.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
