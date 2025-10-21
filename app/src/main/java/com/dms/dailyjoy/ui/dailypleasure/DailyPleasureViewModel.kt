package com.dms.dailyjoy.ui.dailypleasure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.usecase.pleasures.GenerateWeeklyPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetPleasureForTodayUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.UpdatePleasureUseCase
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
class DailyPleasureViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasureForTodayUseCase: GetPleasureForTodayUseCase,
    private val generateWeeklyPleasuresUseCase: GenerateWeeklyPleasuresUseCase,
    private val updatePleasureUseCase: UpdatePleasureUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyPleasureState())
    val state = _state.asStateFlow()

    init {
        loadDailyPleasure()
    }

    fun onDailyCardFlipped() = viewModelScope.launch {
        val updatedPleasure = _state.value.dailyPleasure.copy(isFlipped = true)
        updatePleasureUseCase(updatedPleasure)
        _state.update {
            it.copy(
                dailyPleasure = updatedPleasure,
                waitDonePleasure = true
            )
        }
    }

    fun markDailyCardAsDone() = viewModelScope.launch {
        val updatedPleasure = _state.value.dailyPleasure.copy(isDone = true)
        updatePleasureUseCase(updatedPleasure)
        _state.update {
            it.copy(dailyPleasure = updatedPleasure, waitDonePleasure = false)
        }
    }

    private fun loadDailyPleasure() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        var pleasure = getPleasureForTodayUseCase()

        if (pleasure == null) {
            generateWeeklyPleasuresUseCase()
            pleasure = getPleasureForTodayUseCase()
        }

        _state.update {
            it.copy(
                isLoading = false,
                dailyMessage = getRandomDailyMessageUseCase(),
                dailyPleasure = pleasure ?: Pleasure()
            )
        }
    }
}
