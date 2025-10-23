package com.dms.dailyjoy.ui.weekly

import com.dms.dailyjoy.data.model.Pleasure

data class WeeklyUiState(
    val isLoading: Boolean = false,
    val weeklyPleasures: List<WeeklyPleasureItem> = emptyList(),
    val error: String? = null,
    val selectedPleasure: Pleasure? = null,
    val completedPleasuresCount: Int = 0,
    val remainingPleasuresCount: Int = 0
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

sealed interface WeeklyEvent {
    data class OnCardClicked(val item: WeeklyPleasureItem) : WeeklyEvent
    data object OnBottomSheetDismissed : WeeklyEvent
    data object OnRetryClicked : WeeklyEvent
}
