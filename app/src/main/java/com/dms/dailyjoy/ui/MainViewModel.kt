package com.dms.dailyjoy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.domain.usecase.pleasures.GenerateWeeklyPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetPleasureForTodayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPleasureForTodayUseCase: GetPleasureForTodayUseCase,
    private val generateWeeklyPleasuresUseCase: GenerateWeeklyPleasuresUseCase
) : ViewModel() {

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            var pleasure = getPleasureForTodayUseCase()
            if (pleasure == null) {
                generateWeeklyPleasuresUseCase()
            }
            _isReady.value = true
        }
    }
}