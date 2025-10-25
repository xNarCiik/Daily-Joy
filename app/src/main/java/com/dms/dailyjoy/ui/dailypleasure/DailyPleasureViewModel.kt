package com.dms.dailyjoy.ui.dailypleasure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.domain.usecase.dailypleasure.GetDailyPleasureUseCase
import com.dms.dailyjoy.domain.usecase.dailypleasure.GetRandomPleasureUseCase
import com.dms.dailyjoy.domain.usecase.dailypleasure.SaveDailyPleasureUseCase
import com.dms.dailyjoy.domain.usecase.history.SaveHistoryEntryUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetPleasuresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MinimumPleasuresCount = 7

@HiltViewModel
class DailyPleasureViewModel @Inject constructor(
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val getRandomPleasureUseCase: GetRandomPleasureUseCase,
    private val getDailyPleasureUseCase: GetDailyPleasureUseCase,
    private val saveDailyPleasureUseCase: SaveDailyPleasureUseCase,
    private val saveHistoryEntryUseCase: SaveHistoryEntryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyPleasureUiState())
    val uiState: StateFlow<DailyPleasureUiState> = _uiState.asStateFlow()

    init {
        checkInitialState()
    }

    fun checkInitialState() {
        viewModelScope.launch {
            val pleasures = getPleasuresUseCase().first()

            val enabledPleasures = pleasures.count { it.isEnabled }
            val isSetupRequired = enabledPleasures < MinimumPleasuresCount

            if (isSetupRequired) {
                _uiState.value =
                    DailyPleasureUiState(
                        screenState = DailyPleasureScreenState.SetupRequired(
                            pleasureCount = enabledPleasures
                        )
                    )
                return@launch
            }

            val dailPleasure = getDailyPleasureUseCase().first()
            _uiState.value = DailyPleasureUiState(
                screenState = DailyPleasureScreenState.Ready(
                    availableCategories = PleasureCategory.entries,
                    dailyPleasure = dailPleasure,
                    isCardFlipped = dailPleasure != null
                )
            )
        }
    }

    fun onEvent(event: DailyPleasureEvent) {
        when (event) {
            is DailyPleasureEvent.Reload -> checkInitialState()
            is DailyPleasureEvent.OnCategorySelected -> handleCategorySelection(event.category)
            is DailyPleasureEvent.OnCardClicked -> handleDrawCard()
            is DailyPleasureEvent.OnCardFlipped -> handleCardFlipped()
            is DailyPleasureEvent.OnCardMarkedAsDone -> handleCardMarkedAsDone()
        }
    }

    private fun handleCategorySelection(category: PleasureCategory) = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready) {
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(selectedCategory = category)
            )
        }
    }

    private fun handleDrawCard() = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready && currentState.dailyPleasure == null) {
            val randomPleasure = getRandomPleasureUseCase(currentState.selectedCategory).first()
            saveHistoryEntryUseCase(randomPleasure)
            saveDailyPleasureUseCase(randomPleasure)
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(dailyPleasure = randomPleasure)
            )
        }
    }

    private fun handleCardFlipped() = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready) {
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(isCardFlipped = true)
            )
        }
    }

    private fun handleCardMarkedAsDone() = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready) {
            currentState.dailyPleasure?.let {
                saveHistoryEntryUseCase(pleasure = currentState.dailyPleasure, markAsCompleted = true)
                _uiState.value = _uiState.value.copy(
                    screenState = DailyPleasureScreenState.Completed
                )
            }
        }
    }
}
