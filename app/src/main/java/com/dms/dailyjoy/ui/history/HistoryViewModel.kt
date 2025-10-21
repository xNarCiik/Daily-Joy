package com.dms.dailyjoy.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.R
import com.dms.dailyjoy.domain.model.WeeklyPleasureDetails
import com.dms.dailyjoy.domain.usecase.pleasures.GetWeeklyPleasuresUseCase
import com.dms.dailyjoy.ui.util.getCurrentDayIndex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getWeeklyPleasuresUseCase: GetWeeklyPleasuresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
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
                val weeklyItems = buildWeeklyItems(weeklyDetails)
                val completedCount =
                    weeklyItems.count { it.status == PleasureStatus.PAST_COMPLETED || it.status == PleasureStatus.CURRENT_COMPLETED }
                val remainingCount = 7 - completedCount
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyPleasures = weeklyItems,
                        completedPleasuresCount = completedCount,
                        remainingPleasuresCount = remainingCount,
                        error = null
                    )
                }
            }
    }


    private fun buildWeeklyItems(details: List<WeeklyPleasureDetails>): List<WeeklyPleasureItem> {
        val todayIndex = getCurrentDayIndex()

        val dayRes = listOf(
            R.string.full_day_monday,
            R.string.full_day_tuesday,
            R.string.full_day_wednesday,
            R.string.full_day_thursday,
            R.string.full_day_friday,
            R.string.full_day_saturday,
            R.string.full_day_sunday
        )

        return (0..6).map { dayOfWeek ->
            val detail = details.find { it.dayOfWeek == dayOfWeek }
            val currentDayIndex = dayOfWeek

            val status = when {
                currentDayIndex < todayIndex -> if (detail?.completed == true) PleasureStatus.PAST_COMPLETED else PleasureStatus.PAST_NOT_COMPLETED
                currentDayIndex == todayIndex -> if (detail?.completed == true) PleasureStatus.CURRENT_COMPLETED else if (detail?.pleasure?.isFlipped == true) PleasureStatus.CURRENT_REVEALED else PleasureStatus.LOCKED
                else -> PleasureStatus.LOCKED
            }

            WeeklyPleasureItem(
                dayNameRes = dayRes[currentDayIndex],
                pleasure = detail?.pleasure,
                status = status
            )
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnCardClicked -> {
                if (event.item.status != PleasureStatus.LOCKED) {
                    _uiState.update { it.copy(selectedPleasure = event.item.pleasure) }
                }
            }

            is HistoryEvent.OnBottomSheetDismissed -> {
                _uiState.update { it.copy(selectedPleasure = null) }
            }

            is HistoryEvent.OnRetryClicked -> {
                loadWeeklyHistory()
            }
        }
    }
}
