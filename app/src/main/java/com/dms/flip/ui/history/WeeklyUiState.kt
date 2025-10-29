package com.dms.flip.ui.history

import com.dms.flip.data.database.entity.PleasureHistoryEntry
import java.util.Locale

data class WeeklyUiState(
    val isLoading: Boolean = false,
    val weeklyDays: List<WeeklyDay> = emptyList(),
    val error: String? = null,
    val selectedHistoryEntry: PleasureHistoryEntry? = null
) {
    val isEmpty: Boolean get() = weeklyDays.all { it.historyEntry == null } && !isLoading && error == null
}

data class WeeklyDay(
    val dayName: String,
    val historyEntry: PleasureHistoryEntry?
)

sealed interface WeeklyEvent {
    data object OnRetryClicked : WeeklyEvent
    data class OnCardClicked(val item: PleasureHistoryEntry) : WeeklyEvent
    data object OnBottomSheetDismissed : WeeklyEvent
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
