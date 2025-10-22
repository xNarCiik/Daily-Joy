package com.dms.dailyjoy.ui.dailypleasure

import com.dms.dailyjoy.data.model.Pleasure

data class DailyPleasureUiState(
    val isLoading: Boolean = true,
    val headerMessage: String = "",
    val dailyPleasure: Pleasure = Pleasure()
)

sealed interface DailyPleasureEvent {
    data object OnCardFlipped : DailyPleasureEvent
    data object OnCardMarkedAsDone : DailyPleasureEvent
}
