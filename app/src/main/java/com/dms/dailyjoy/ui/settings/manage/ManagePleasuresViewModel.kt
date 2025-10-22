package com.dms.dailyjoy.ui.settings.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.domain.usecase.pleasures.AddCustomPleasureUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.DeleteCustomPleasureUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.UpdatePleasureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagePleasuresViewModel @Inject constructor(
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val updatePleasureUseCase: UpdatePleasureUseCase,
    private val addCustomPleasureUseCase: AddCustomPleasureUseCase,
    private val deleteCustomPleasureUseCase: DeleteCustomPleasureUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManagePleasuresUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAndFilterPleasures()
    }

    private fun loadAndFilterPleasures() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            getPleasuresUseCase().collectLatest { pleasures ->
                _uiState.update {
                    it.copy(
                        allPleasures = pleasures,
                        isLoading = false,
                        error = null
                    )
                }
                updateFilteredPleasures()
            }
        } catch (e: Exception) {
            _uiState.update {
                // TODO STRING HERE
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }

    private fun updateFilteredPleasures() {
        _uiState.update { state ->
            val filtered = state.allPleasures.filter { pleasure ->
                val typeMatches = when (state.selectedTab) {
                    ManagePleasuresTab.SMALL -> pleasure.type == PleasureType.SMALL
                    ManagePleasuresTab.BIG -> pleasure.type == PleasureType.BIG
                }
                val categoryMatches = state.selectedCategories.contains(pleasure.category)
                typeMatches && categoryMatches
            }
            state.copy(filteredPleasures = filtered)
        }
    }

    fun onEvent(event: ManagePleasuresEvent) {
        when (event) {
            is ManagePleasuresEvent.OnPleasureToggled -> togglePleasure(event.pleasure)
            is ManagePleasuresEvent.OnAddPleasureClicked -> _uiState.update { it.copy(showAddDialog = true) }
            is ManagePleasuresEvent.OnBottomSheetDismissed -> dismissBottomSheet()
            is ManagePleasuresEvent.OnTitleChanged -> _uiState.update {
                it.copy(
                    newPleasureTitle = event.title,
                    titleError = null
                )
            }

            is ManagePleasuresEvent.OnDescriptionChanged -> _uiState.update {
                it.copy(
                    newPleasureDescription = event.description,
                    descriptionError = null
                )
            }

            is ManagePleasuresEvent.OnCategoryChanged -> _uiState.update {
                it.copy(
                    newPleasureCategory = event.category
                )
            }

            is ManagePleasuresEvent.OnSavePleasureClicked -> savePleasure()
            is ManagePleasuresEvent.OnDeletePleasureClicked -> _uiState.update {
                it.copy(
                    pleasureToDelete = event.pleasure,
                    showDeleteConfirmation = true
                )
            }

            is ManagePleasuresEvent.OnDeleteConfirmed -> confirmDelete()
            is ManagePleasuresEvent.OnDeleteCancelled -> cancelDelete()
            is ManagePleasuresEvent.OnTabSelected -> {
                _uiState.update { it.copy(selectedTab = event.tab) }
                updateFilteredPleasures()
            }

            is ManagePleasuresEvent.OnCategoryFilterChanged -> {
                toggleCategoryFilter(event.category)
                updateFilteredPleasures()
            }

            is ManagePleasuresEvent.OnRetryClicked -> loadAndFilterPleasures()
        }
    }

    private fun togglePleasure(pleasure: Pleasure) = viewModelScope.launch {
        try {
            val updatedPleasure = pleasure.copy(isEnabled = !pleasure.isEnabled)
            updatePleasureUseCase(updatedPleasure)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message ?: "Erreur lors de la mise à jour") }
        }
    }

    private fun dismissBottomSheet() {
        _uiState.update {
            it.copy(
                showAddDialog = false,
                newPleasureTitle = "",
                newPleasureDescription = "",
                newPleasureCategory = PleasureCategory.FOOD,
                titleError = null,
                descriptionError = null
            )
        }
    }

    private fun savePleasure() {
        val state = _uiState.value
        var hasError = false

        if (state.newPleasureTitle.isBlank()) {
            _uiState.update { it.copy(titleError = "Le titre ne peut pas être vide") }
            hasError = true
        }

        if (state.newPleasureDescription.isBlank()) {
            _uiState.update { it.copy(descriptionError = "La description ne peut pas être vide") }
            hasError = true
        }

        if (hasError) return

        val type = when (state.selectedTab) {
            ManagePleasuresTab.SMALL -> PleasureType.SMALL
            ManagePleasuresTab.BIG -> PleasureType.BIG
        }

        viewModelScope.launch {
            try {
                addCustomPleasureUseCase(
                    title = state.newPleasureTitle.trim(),
                    description = state.newPleasureDescription.trim(),
                    category = state.newPleasureCategory,
                    type = type
                )
                dismissBottomSheet()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Erreur lors de l'ajout") }
            }
        }
    }

    private fun confirmDelete() {
        _uiState.value.pleasureToDelete?.let { pleasure ->
            viewModelScope.launch {
                try {
                    deleteCustomPleasureUseCase(pleasure)
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            error = e.message ?: "Erreur lors de la suppression"
                        )
                    }
                }
            }
        }
        cancelDelete()
    }

    private fun cancelDelete() {
        _uiState.update { it.copy(showDeleteConfirmation = false, pleasureToDelete = null) }
    }

    private fun toggleCategoryFilter(category: PleasureCategory) {
        _uiState.update { state ->
            val updatedCategories = state.selectedCategories.toMutableSet()
            if (category in updatedCategories) {
                updatedCategories.remove(category)
            } else {
                updatedCategories.add(category)
            }
            state.copy(selectedCategories = updatedCategories)
        }
    }
}
