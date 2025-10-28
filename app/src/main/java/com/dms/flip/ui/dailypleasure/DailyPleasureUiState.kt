package com.dms.flip.ui.dailypleasure

import com.dms.flip.data.model.Pleasure
import com.dms.flip.data.model.PleasureCategory

data class DailyPleasureUiState(
    val screenState: DailyPleasureScreenState = DailyPleasureScreenState.Loading,
    val headerMessage: String = "",
)

sealed interface DailyPleasureScreenState {
    data object Loading : DailyPleasureScreenState

    data class SetupRequired(val pleasureCount: Int) : DailyPleasureScreenState

    data class Ready(
        val availableCategories: List<PleasureCategory> = emptyList(),
        val selectedCategory: PleasureCategory = PleasureCategory.ALL,
        val dailyPleasure: Pleasure? = null,
        val isCardFlipped: Boolean = false
    ) : DailyPleasureScreenState

    data object Completed : DailyPleasureScreenState
}

sealed interface DailyPleasureEvent {
    data object Reload : DailyPleasureEvent
    data class OnCategorySelected(val category: PleasureCategory) : DailyPleasureEvent
    data object OnCardClicked : DailyPleasureEvent
    data object OnCardFlipped : DailyPleasureEvent
    data object OnCardMarkedAsDone : DailyPleasureEvent
}
