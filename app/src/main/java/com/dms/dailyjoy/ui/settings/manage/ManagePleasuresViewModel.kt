package com.dms.dailyjoy.ui.settings.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.domain.usecase.AddCustomPleasureUseCase
import com.dms.dailyjoy.domain.usecase.DeleteCustomPleasureUseCase
import com.dms.dailyjoy.domain.usecase.GetPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.UpdatePleasureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagePleasuresViewModel @Inject constructor(
    getPleasuresUseCase: GetPleasuresUseCase,
    private val updatePleasureUseCase: UpdatePleasureUseCase,
    private val addCustomPleasureUseCase: AddCustomPleasureUseCase,
    private val deleteCustomPleasureUseCase: DeleteCustomPleasureUseCase
) : ViewModel() {

    val pleasures: StateFlow<List<Pleasure>> = getPleasuresUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updatePleasure(pleasure: Pleasure) = viewModelScope.launch {
        updatePleasureUseCase(pleasure)
    }

    fun addPleasure(
        title: String,
        description: String,
        category: PleasureCategory,
        type: PleasureType
    ) = viewModelScope.launch {
        addCustomPleasureUseCase(title, description, category, type)
    }

    fun deletePleasure(pleasure: Pleasure) = viewModelScope.launch {
        deleteCustomPleasureUseCase(pleasure)
    }

}
