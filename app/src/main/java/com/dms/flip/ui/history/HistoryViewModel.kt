package com.dms.flip.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.data.database.entity.PleasureHistoryEntry
import com.dms.flip.domain.usecase.weekly.GetWeeklyHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getWeeklyHistoryUseCase: GetWeeklyHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadWeeklyHistory()
    }

    private fun loadWeeklyHistory() = viewModelScope.launch {
        getWeeklyHistoryUseCase()
            .onStart {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }
            .catch { throwable ->
                _uiState.update {
                    it.copy(isLoading = false, error = throwable.message ?: "An unknown error occurred")
                }
            }
            .collect { historyEntries ->
                val weeklyDays = generateFullWeek(historyEntries)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyDays = weeklyDays,
                        error = null
                    )
                }
            }
    }

    private fun generateFullWeek(historyEntries: List<PleasureHistoryEntry>): List<WeeklyDay> {
        val days = (1..7).map { dayOfWeek ->
            val dayName = getDayName(dayOfWeek)
            val historyEntry = historyEntries.find {
                val entryCalendar = Calendar.getInstance()
                entryCalendar.timeInMillis = it.dateDrawn
                val entryDayOfWeek = (entryCalendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7 + 1
                entryDayOfWeek == dayOfWeek
            }
            WeeklyDay(dayName, historyEntry)
        }

        // Reorder days to start from Monday and end on Sunday
        val shiftedDays = mutableListOf<WeeklyDay>()
        shiftedDays.addAll(days.filter { it.dayName != "Lundi" && it.dayName != "Mardi" && it.dayName != "Mercredi" && it.dayName != "Jeudi" && it.dayName != "Vendredi" && it.dayName != "Samedi" && it.dayName != "Dimanche" })
        shiftedDays.addAll(days.filter { it.dayName == "Lundi" || it.dayName == "Mardi" || it.dayName == "Mercredi" || it.dayName == "Jeudi" || it.dayName == "Vendredi" || it.dayName == "Samedi" || it.dayName == "Dimanche" })
        return shiftedDays
    }


    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnRetryClicked -> {
                loadWeeklyHistory()
            }
            is HistoryEvent.OnCardClicked -> {
                _uiState.update { it.copy(selectedHistoryEntry = event.item) }
            }
            is HistoryEvent.OnBottomSheetDismissed -> {
                _uiState.update { it.copy(selectedHistoryEntry = null) }
            }

            else -> {
                // TODO: À implémenter dans le ViewModel) ==========
            }
        }
    }
}
