package com.dms.dailyjoy.ui.weekly

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.domain.usecase.weekly.GetWeeklyHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyViewModel @Inject constructor(
    private val getWeeklyHistoryUseCase: GetWeeklyHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyUiState())
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
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "An unknown error occurred"
                    )
                }
            }
            .collect { historyEntries ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        history = historyEntries,
                        error = null
                    )
                }
            }
    }

    fun onEvent(event: WeeklyEvent) {
        when (event) {
            is WeeklyEvent.OnRetryClicked -> {
                loadWeeklyHistory()
            }

            is WeeklyEvent.OnBottomSheetDismissed -> {
                TODO()
            }

            is WeeklyEvent.OnCardClicked -> {
                TODO()
            }
        }
    }
}
