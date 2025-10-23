package com.dms.dailyjoy.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.domain.usecase.weekly.BuildWeeklyPleasureItemsUseCase
import com.dms.dailyjoy.domain.usecase.weekly.GetWeeklyPleasuresStatsUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetWeeklyPleasuresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Weekly @Inject constructor(
    private val getWeeklyPleasuresUseCase: GetWeeklyPleasuresUseCase,
    private val buildWeeklyPleasureItemsUseCase: BuildWeeklyPleasureItemsUseCase,
    private val getWeeklyPleasuresStatsUseCase: GetWeeklyPleasuresStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadWeeklyHistory()
    }

    private fun loadWeeklyHistory() = viewModelScope.launch {
        getWeeklyPleasuresUseCase()
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .catch { throwable ->
                _uiState.update { it.copy(isLoading = false, error = throwable.message) }
            }
            .collect { weeklyDetails ->
                val weeklyItems = buildWeeklyPleasureItemsUseCase(weeklyDetails)
                val stats = getWeeklyPleasuresStatsUseCase(weeklyItems)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyPleasures = weeklyItems,
                        completedPleasuresCount = stats.completed,
                        remainingPleasuresCount = stats.remaining,
                        error = null
                    )
                }
            }
    }

    fun onEvent(event: WeeklyEvent) {
        when (event) {
            is WeeklyEvent.OnCardClicked -> {
                if (event.item.status != PleasureStatus.LOCKED) {
                    _uiState.update { it.copy(selectedPleasure = event.item.pleasure) }
                }
            }

            is WeeklyEvent.OnBottomSheetDismissed -> {
                _uiState.update { it.copy(selectedPleasure = null) }
            }

            is WeeklyEvent.OnRetryClicked -> {
                loadWeeklyHistory()
            }
        }
    }
}
