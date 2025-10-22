package com.dms.dailyjoy.ui.dailypleasure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GenerateWeeklyPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetPleasureForTodayUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.UpdatePleasureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyPleasureViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasureForTodayUseCase: GetPleasureForTodayUseCase,
    private val generateWeeklyPleasuresUseCase: GenerateWeeklyPleasuresUseCase,
    private val updatePleasureUseCase: UpdatePleasureUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyPleasureUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDailyPleasure()
    }

    fun onEvent(event: DailyPleasureEvent) {
        when (event) {
            is DailyPleasureEvent.OnCardFlipped -> {
                onDailyCardFlipped()
            }

            is DailyPleasureEvent.OnCardMarkedAsDone -> {
                markDailyCardAsDone()
            }
        }
    }

    private fun loadDailyPleasure() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        var pleasure = getPleasureForTodayUseCase()

        if (pleasure == null) {
            generateWeeklyPleasuresUseCase()
            pleasure = getPleasureForTodayUseCase()
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                headerMessage = getHeaderMessage(pleasure = pleasure),
                dailyPleasure = pleasure ?: Pleasure()
            )
        }
    }

    private fun getHeaderMessage(pleasure: Pleasure?) = when {
        pleasure?.isDone == true -> "Et si on allez voir ceux des autres en attendant ?"
        pleasure?.isFlipped == true -> "Swipper vers la droite une fois le plaisir réalisé !" // TODO STRING
        else -> getRandomDailyMessageUseCase()
    }

    private fun onDailyCardFlipped() = viewModelScope.launch {
        val updatedPleasure = _uiState.value.dailyPleasure.copy(isFlipped = true)
        updatePleasureUseCase(updatedPleasure)
        _uiState.update {
            it.copy(
                dailyPleasure = updatedPleasure,
                headerMessage = getHeaderMessage(pleasure = updatedPleasure)
            )
        }
    }

    private fun markDailyCardAsDone() = viewModelScope.launch {
        val updatedPleasure = _uiState.value.dailyPleasure.copy(isDone = true)
        updatePleasureUseCase(updatedPleasure)
        _uiState.update {
            it.copy(
                dailyPleasure = updatedPleasure,
                headerMessage = getHeaderMessage(pleasure = updatedPleasure)
            )
        }
    }
}
