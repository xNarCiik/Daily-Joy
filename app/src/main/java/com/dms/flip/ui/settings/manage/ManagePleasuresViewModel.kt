package com.dms.flip.ui.settings.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.usecase.pleasures.AddCustomPleasureUseCase
import com.dms.flip.domain.usecase.pleasures.DeletePleasureUseCase
import com.dms.flip.domain.usecase.pleasures.GetPleasuresUseCase
import com.dms.flip.domain.usecase.pleasures.UpdatePleasureUseCase
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
    private val deletePleasureUseCase: DeletePleasureUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManagePleasuresUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPleasures()
    }

    private fun loadPleasures() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            getPleasuresUseCase().collectLatest { pleasures ->
                _uiState.update {
                    it.copy(
                        pleasures = pleasures,
                        isLoading = false,
                        error = null
                    )
                }
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
            is ManagePleasuresEvent.OnDeleteMultiplePleasuresClicked -> {
                // TODO
            }

            is ManagePleasuresEvent.OnDeleteConfirmed -> confirmDelete()
            is ManagePleasuresEvent.OnDeleteCancelled -> cancelDelete()


            is ManagePleasuresEvent.OnRetryClicked -> loadPleasures()
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

        viewModelScope.launch {
            try {
                addCustomPleasureUseCase(
                    title = state.newPleasureTitle.trim(),
                    description = state.newPleasureDescription.trim(),
                    category = state.newPleasureCategory
                )
                dismissBottomSheet()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Erreur lors de l'ajout") }
            }
        }
    }

    private fun confirmDelete() {
       /* _uiState.value.pleasureToDelete?.let { pleasure ->
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
        cancelDelete() */
    }

    private fun cancelDelete() {
        _uiState.update { it.copy(showDeleteConfirmation = false) } // TODO
    }
}
