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
import kotlinx.coroutines.flow.first
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

        val pleasures = getPleasuresUseCase().first()
        _uiState.update {
            it.copy(
                isLoading = false,
                pleasures = pleasures
            )
        }
    }

    fun onEvent(event: ManagePleasuresEvent) {
        when (event) {
            is ManagePleasuresEvent.OnRetryClicked -> {
            }

            is ManagePleasuresEvent.OnBottomSheetDismissed -> {
            }
        }
    }

    private fun updatePleasure(pleasure: Pleasure) = viewModelScope.launch {
        updatePleasureUseCase(pleasure)
    }

    private fun addPleasure(
        title: String,
        description: String,
        category: PleasureCategory,
        type: PleasureType
    ) = viewModelScope.launch {
        addCustomPleasureUseCase(title, description, category, type)
    }

    // TODO call in screen and check (we dont want to edit a pleasure currently use in weekly
    private fun deletePleasure(pleasure: Pleasure) = viewModelScope.launch {
        deleteCustomPleasureUseCase(pleasure)
    }
}
