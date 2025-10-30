package com.dms.flip.ui.history

import com.dms.flip.data.model.PleasureHistoryEntry
import java.util.Locale

data class HistoryUiState(
    val isLoading: Boolean = false,
    val weeklyDays: List<WeeklyDay> = emptyList(),
    val error: String? = null,
    val selectedHistoryEntry: PleasureHistoryEntry? = null,

    // ========== NOUVELLES PROPRIÉTÉS (TODO: À implémenter dans le ViewModel) ==========
    // TODO: Calculer dans le ViewModel en fonction de la semaine sélectionnée
    val weekTitle: String = "Cette Semaine", // Ex: "Cette Semaine", "Semaine dernière", etc.

    // TODO: Calculer dans le ViewModel (ex: "17 - 23 Juin")
    val weekDates: String = "",

    // TODO: Calculer dans le ViewModel - Nombre de jours consécutifs avec un plaisir complété
    val streakDays: Int = 0,

    // TODO: Ajouter un offset pour la navigation entre les semaines (0 = semaine courante, -1 = semaine précédente, etc.)
    val weekOffset: Int = 0
)

data class WeeklyDay(
    val dayName: String,
    val historyEntry: PleasureHistoryEntry?
)

sealed interface HistoryEvent {
    data object OnRetryClicked : HistoryEvent
    data class OnCardClicked(val item: PleasureHistoryEntry) : HistoryEvent
    data object OnBottomSheetDismissed : HistoryEvent
    data object OnPreviousWeekClicked : HistoryEvent
    data object OnNextWeekClicked : HistoryEvent
    data object OnDiscoverTodayClicked : HistoryEvent
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
