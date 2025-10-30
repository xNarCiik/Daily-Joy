package com.dms.flip.ui.settings.manage

import com.dms.flip.domain.model.Pleasure
import com.dms.flip.data.model.PleasureCategory

data class ManagePleasuresUiState(
    val isLoading: Boolean = false,
    val pleasures: List<Pleasure> = emptyList(),
    val showAddDialog: Boolean = false,
    val newPleasureTitle: String = "",
    val newPleasureDescription: String = "",
    val newPleasureCategory: PleasureCategory = PleasureCategory.FOOD,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val showDeleteConfirmation: Boolean = false,
    val error: String? = null
)

sealed interface ManagePleasuresEvent {
    data class OnPleasureToggled(val pleasure: Pleasure) : ManagePleasuresEvent
    data object OnAddPleasureClicked : ManagePleasuresEvent
    data object OnBottomSheetDismissed : ManagePleasuresEvent
    data class OnTitleChanged(val title: String) : ManagePleasuresEvent
    data class OnDescriptionChanged(val description: String) : ManagePleasuresEvent
    data class OnCategoryChanged(val category: PleasureCategory) : ManagePleasuresEvent
    data object OnSavePleasureClicked : ManagePleasuresEvent
    data class OnDeleteMultiplePleasuresClicked(val pleasuresId: List<Int>) : ManagePleasuresEvent
    data object OnDeleteConfirmed : ManagePleasuresEvent
    data object OnDeleteCancelled : ManagePleasuresEvent
    data object OnRetryClicked : ManagePleasuresEvent
}
