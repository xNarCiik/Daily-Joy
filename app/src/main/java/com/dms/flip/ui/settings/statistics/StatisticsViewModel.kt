package com.dms.flip.ui.settings.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.R
import com.dms.flip.domain.usecase.statistics.GetStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        getStatisticsUseCase()
            .catch { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = R.string.generic_error_message
                    )
                }
            }
            .collectLatest { stats ->
                _uiState.update { stats }
            }
    }

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            StatisticsEvent.OnRetryClicked -> loadStatistics()
        }
    }
}
