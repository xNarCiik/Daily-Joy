package com.dms.dailyjoy.ui.weekly

import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry

data class WeeklyUiState(
    val isLoading: Boolean = false,
    val history: List<PleasureHistoryEntry> = emptyList(),
    val error: String? = null
) {
    val isEmpty: Boolean get() = history.isEmpty() && !isLoading && error == null
}

sealed interface WeeklyEvent {
    data object OnRetryClicked : WeeklyEvent
    data class OnCardClicked(val item: PleasureHistoryEntry) : WeeklyEvent
    data object OnBottomSheetDismissed : WeeklyEvent
}
