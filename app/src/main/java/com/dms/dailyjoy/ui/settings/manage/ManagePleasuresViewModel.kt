package com.dms.dailyjoy.ui.settings.manage

import androidx.compose.ui.graphics.Color
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
        loadPleasures()
    }

    private fun loadPleasures() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            getPleasuresUseCase().collect { pleasures ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pleasures = pleasures,
                        error = null
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Une erreur est survenue"
                )
            }
        }
    }

    fun onEvent(event: ManagePleasuresEvent) {
        when (event) {
            is ManagePleasuresEvent.OnPleasureToggled -> {
                togglePleasure(event.pleasure)
            }
            is ManagePleasuresEvent.OnAddPleasureClicked -> {
                _uiState.update { it.copy(showAddDialog = true) }
            }
            is ManagePleasuresEvent.OnBottomSheetDismissed -> {
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
            is ManagePleasuresEvent.OnTitleChanged -> {
                _uiState.update {
                    it.copy(
                        newPleasureTitle = event.title,
                        titleError = null
                    )
                }
            }
            is ManagePleasuresEvent.OnDescriptionChanged -> {
                _uiState.update {
                    it.copy(
                        newPleasureDescription = event.description,
                        descriptionError = null
                    )
                }
            }
            is ManagePleasuresEvent.OnCategoryChanged -> {
                _uiState.update { it.copy(newPleasureCategory = event.category) }
            }
            is ManagePleasuresEvent.OnSavePleasureClicked -> {
                savePleasure()
            }
            is ManagePleasuresEvent.OnDeletePleasureClicked -> {
                _uiState.update {
                    it.copy(
                        pleasureToDelete = event.pleasure,
                        showDeleteConfirmation = true
                    )
                }
            }
            is ManagePleasuresEvent.OnDeleteConfirmed -> {
                _uiState.value.pleasureToDelete?.let { pleasure ->
                    deletePleasure(pleasure)
                }
                _uiState.update {
                    it.copy(
                        showDeleteConfirmation = false,
                        pleasureToDelete = null
                    )
                }
            }
            is ManagePleasuresEvent.OnDeleteCancelled -> {
                _uiState.update {
                    it.copy(
                        showDeleteConfirmation = false,
                        pleasureToDelete = null
                    )
                }
            }
            is ManagePleasuresEvent.OnTabSelected -> {
                _uiState.update { it.copy(selectedTab = event.tab) }
            }
            is ManagePleasuresEvent.OnRetryClicked -> {
                loadPleasures()
            }
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
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = e.message ?: "Erreur lors de l'ajout")
                }
            }
        }
    }

    private fun deletePleasure(pleasure: Pleasure) = viewModelScope.launch {
        try {
            deleteCustomPleasureUseCase(pleasure)
        } catch (e: Exception) {
            _uiState.update {
                it.copy(error = e.message ?: "Erreur lors de la suppression")
            }
        }
    }
}

/**
 * Extension functions and utilities for PleasureCategory
 */

data class CategoryInfo(
    val label: String,
    val color: Color
)

fun PleasureCategory.toCategoryInfo(): CategoryInfo {
    return when (this) {
        PleasureCategory.FOOD -> CategoryInfo(
            label = "Nourriture",
            color = Color(0xFFFF9800)
        )
        PleasureCategory.ENTERTAINMENT -> CategoryInfo(
            label = "Divertissement",
            color = Color(0xFF9C27B0)
        )
        PleasureCategory.SOCIAL -> CategoryInfo(
            label = "Social",
            color = Color(0xFF2196F3)
        )
        PleasureCategory.WELLNESS -> CategoryInfo(
            label = "Bien-être",
            color = Color(0xFF4CAF50)
        )
        PleasureCategory.CREATIVE -> CategoryInfo(
            label = "Créatif",
            color = Color(0xFFE91E63)
        )
        PleasureCategory.OUTDOOR -> CategoryInfo(
            label = "Extérieur",
            color = Color(0xFF009688)
        )
        PleasureCategory.OTHER -> CategoryInfo(
            label = "Autre",
            color = Color(0xFF607D8B)
        )

        // TODO
        else -> CategoryInfo(
            label = "Autre",
            color = Color(0xFF607D8B)
        )
    }
}

fun PleasureCategory.getLabel(): String = this.toCategoryInfo().label

fun PleasureCategory.getColor(): Color = this.toCategoryInfo().color

/**
 * Get all categories with their display information
 */
fun getAllCategoriesInfo(): List<Pair<PleasureCategory, CategoryInfo>> {
    return PleasureCategory.entries.map { category ->
        category to category.toCategoryInfo()
    }
}
