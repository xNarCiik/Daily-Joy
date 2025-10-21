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

    // TODO call in screen and check (we dont want to edit a pleasure currently use in weekly
    fun deletePleasure(pleasure: Pleasure) = viewModelScope.launch {
        deleteCustomPleasureUseCase(pleasure)
    }

}
