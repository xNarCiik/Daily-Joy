package com.dms.dailyjoy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureHistoryRepository
import com.dms.dailyjoy.domain.usecase.GetPleasureForTodayUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DailyPleasureState(
    val dailyMessage: String = "",
    val dailyPleasure: Pleasure = Pleasure(),
    val waitDonePleasure: Boolean = false
)

@HiltViewModel
class PleasureViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasureForTodayUseCase: GetPleasureForTodayUseCase,
    private val pleasureHistoryRepository: PleasureHistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DailyPleasureState())
    val state = _state.asStateFlow()

    init {
        loadDailyPleasure()
    }

    fun loadDailyPleasure() = viewModelScope.launch {
        val pleasure = getPleasureForTodayUseCase() ?: Pleasure()
        pleasureHistoryRepository.save(pleasure)
        _state.update {
            DailyPleasureState(
                dailyMessage = getRandomDailyMessageUseCase(),
                dailyPleasure = pleasure
            )
        }
    }

    fun onDailyCardFlipped() = viewModelScope.launch {
        val updatedPleasure = _state.value.dailyPleasure.copy(isFlipped = true)
        pleasureHistoryRepository.save(updatedPleasure)
        _state.update {
            it.copy(
                dailyPleasure = updatedPleasure,
                waitDonePleasure = true
            )
        }
    }

    fun markDailyCardAsDone() = viewModelScope.launch {
        val updatedPleasure = _state.value.dailyPleasure.copy(isDone = true)
        pleasureHistoryRepository.save(updatedPleasure)
        _state.update {
            it.copy(dailyPleasure = updatedPleasure, waitDonePleasure = false)
        }
    }
}
