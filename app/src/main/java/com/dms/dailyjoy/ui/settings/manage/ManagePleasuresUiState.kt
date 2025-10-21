package com.dms.dailyjoy.ui.settings.manage

import com.dms.dailyjoy.data.model.Pleasure

data class ManagePleasuresUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val pleasures: List<Pleasure> = listOf()
)

sealed interface ManagePleasuresEvent {
    // TODO
    data object OnBottomSheetDismissed : ManagePleasuresEvent
    data object OnRetryClicked : ManagePleasuresEvent
}
