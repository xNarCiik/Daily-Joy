package com.dms.dailyjoy.ui.dailypleasure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.usecase.GenerateWeeklyPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.GetPleasureForTodayUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DailyPleasureState(
    val isLoading: Boolean = true,
    val dailyMessage: String = "",
    val dailyPleasure: Pleasure = Pleasure(),
    val waitDonePleasure: Boolean = false
)

@HiltViewModel
class PleasureViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasureForTodayUseCase: GetPleasureForTodayUseCase,
    private val generateWeeklyPleasuresUseCase: GenerateWeeklyPleasuresUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyPleasureState())
    val state = _state.asStateFlow()

    init {
        loadDailyPleasure()
    }

    fun onDailyCardFlipped() = viewModelScope.launch {
        val updatedPleasure = _state.value.dailyPleasure.copy(isFlipped = true)
       // TODO FIX THAT pleasureHistoryRepository.save(updatedPleasure)
        _state.update {
            it.copy(
                dailyPleasure = updatedPleasure,
                waitDonePleasure = true
            )
        }
    }

    fun markDailyCardAsDone() = viewModelScope.launch {
        val updatedPleasure = _state.value.dailyPleasure.copy(isDone = true)
        // TODO FIX THAT pleasureHistoryRepository.save(updatedPleasure)
        _state.update {
            it.copy(dailyPleasure = updatedPleasure, waitDonePleasure = false)
        }
    }

    private fun loadDailyPleasure() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        val pleasure = getPleasureForTodayUseCase() ?: Pleasure()
        // TODO FIX THAT pleasureHistoryRepository.save(pleasure)

        _state.update {
            it.copy(
                isLoading = false,
                dailyMessage = getRandomDailyMessageUseCase(),
                dailyPleasure = pleasure
            )
        }
    }
}
