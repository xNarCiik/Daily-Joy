package com.dms.dailyjoy.ui.history

import com.dms.dailyjoy.data.model.Pleasure

data class HistoryUiState(
    val isLoading: Boolean = true,
    val weeklyPleasures: List<WeeklyPleasureItem> = emptyList(),
    val error: String? = null
) {
    val isEmpty = !isLoading && weeklyPleasures.isEmpty()
}

data class WeeklyPleasureItem(
    val dayNameRes: Int,
    val pleasure: Pleasure?,
    val status: PleasureStatus
)

enum class PleasureStatus {
    PAST_COMPLETED,
    PAST_NOT_COMPLETED,
    CURRENT_COMPLETED,
    CURRENT_REVEALED,
    LOCKED
}

sealed interface HistoryEvent {
    data class OnCardClicked(val item: WeeklyPleasureItem) : HistoryEvent
    data object OnRetryClicked : HistoryEvent
}
