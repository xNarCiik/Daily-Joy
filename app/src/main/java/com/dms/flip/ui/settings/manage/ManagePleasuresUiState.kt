package com.dms.flip.ui.settings.manage

import com.dms.flip.data.model.Pleasure
import com.dms.flip.data.model.PleasureCategory

data class ManagePleasuresUiState(
    val isLoading: Boolean = false,
    val allPleasures: List<Pleasure> = emptyList(),
    val filteredPleasures: List<Pleasure> = emptyList(),
    val selectedTab: ManagePleasuresTab = ManagePleasuresTab.SMALL,
    val selectedCategories: Set<PleasureCategory> = PleasureCategory.entries.toSet(), // Default to all selected
    val showAddDialog: Boolean = false,
    val newPleasureTitle: String = "",
    val newPleasureDescription: String = "",
    val newPleasureCategory: PleasureCategory = PleasureCategory.FOOD,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val showDeleteConfirmation: Boolean = false,
    val pleasureToDelete: Pleasure? = null,
    val error: String? = null
)

enum class ManagePleasuresTab {
    SMALL, BIG
}

sealed interface ManagePleasuresEvent {
    data class OnPleasureToggled(val pleasure: Pleasure) : ManagePleasuresEvent
    data object OnAddPleasureClicked : ManagePleasuresEvent
    data object OnBottomSheetDismissed : ManagePleasuresEvent
    data class OnTitleChanged(val title: String) : ManagePleasuresEvent
    data class OnDescriptionChanged(val description: String) : ManagePleasuresEvent
    data class OnCategoryChanged(val category: PleasureCategory) : ManagePleasuresEvent
    data object OnSavePleasureClicked : ManagePleasuresEvent
    data class OnDeletePleasureClicked(val pleasure: Pleasure) : ManagePleasuresEvent
    data object OnDeleteConfirmed : ManagePleasuresEvent
    data object OnDeleteCancelled : ManagePleasuresEvent
    data class OnTabSelected(val tab: ManagePleasuresTab) : ManagePleasuresEvent
    data class OnCategoryFilterChanged(val category: PleasureCategory) : ManagePleasuresEvent
    data object OnRetryClicked : ManagePleasuresEvent
}
