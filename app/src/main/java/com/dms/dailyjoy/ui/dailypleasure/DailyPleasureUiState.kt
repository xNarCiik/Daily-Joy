package com.dms.dailyjoy.ui.dailypleasure

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory

data class DailyPleasureUiState(
    val isLoading: Boolean = true,
    val headerMessage: String = "",
    val dailyPleasure: Pleasure = Pleasure(),
    val screenState: DailyPleasureScreenState = DailyPleasureScreenState.Ready,
    val totalPleasuresCount: Int = 0,
    val selectedCategory: PleasureCategory? = null,
    val availableCategories: List<PleasureCategory> = PleasureCategory.entries
)

enum class DailyPleasureScreenState {
    Setup,
    Ready,
    Completed
}

sealed interface DailyPleasureEvent {
    data object OnCardFlipped : DailyPleasureEvent
    data object OnCardMarkedAsDone : DailyPleasureEvent
    data class OnCategorySelected(val category: PleasureCategory) : DailyPleasureEvent
}
