package com.dms.flip.ui.dailyflip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.data.database.mapper.toPleasure
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.domain.usecase.GetRandomDailyMessageUseCase
import com.dms.flip.domain.usecase.dailypleasure.GetRandomPleasureUseCase
import com.dms.flip.domain.usecase.history.GetTodayHistoryEntryUseCase
import com.dms.flip.domain.usecase.history.SaveHistoryEntryUseCase
import com.dms.flip.domain.usecase.pleasures.GetPleasuresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MinimumPleasuresCount = 7

@HiltViewModel
class DailyFlipViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val getRandomPleasureUseCase: GetRandomPleasureUseCase,
    private val saveHistoryEntryUseCase: SaveHistoryEntryUseCase,
    private val getTodayHistoryEntryUseCase: GetTodayHistoryEntryUseCase
) : ViewModel() {

    private val randomDailyMessage = MutableStateFlow("")

    private val _uiState = MutableStateFlow(DailyFlipUiState())
    val uiState: StateFlow<DailyFlipUiState> = _uiState.asStateFlow()

    init {
        checkInitialState()
    }

    fun checkInitialState() {
        viewModelScope.launch {
            val pleasures = getPleasuresUseCase().first()

            if (randomDailyMessage.value.isBlank()) {
                randomDailyMessage.value = getRandomDailyMessageUseCase()
            }

            val enabledPleasures = pleasures.count { it.isEnabled }
            val isSetupRequired = enabledPleasures < MinimumPleasuresCount

            if (isSetupRequired) {
                _uiState.value =
                    DailyFlipUiState(
                        screenState = DailyFlipScreenState.SetupRequired(
                            pleasureCount = enabledPleasures
                        ),
                        headerMessage = "À deux pas de l'aventure..."
                    )
                return@launch
            }

            val todayHistoryPleasure = getTodayHistoryEntryUseCase().first()
            if (todayHistoryPleasure?.isCompleted == true) {
                _uiState.value = _uiState.value.copy(
                    screenState = DailyFlipScreenState.Completed,
                    headerMessage = ""
                )
            } else {
                _uiState.value = DailyFlipUiState(
                    screenState = DailyFlipScreenState.Ready(
                        availableCategories = PleasureCategory.entries,
                        dailyPleasure = todayHistoryPleasure?.toPleasure(),
                        isCardFlipped = todayHistoryPleasure != null
                    ),
                    headerMessage = if (todayHistoryPleasure == null) randomDailyMessage.value else "Votre plaisir du jour"
                )
            }
        }
    }

    fun onEvent(event: DailyFlipEvent) {
        when (event) {
            is DailyFlipEvent.Reload -> checkInitialState()
            is DailyFlipEvent.OnCategorySelected -> handleCategorySelection(event.category)
            is DailyFlipEvent.OnCardClicked -> handleDrawCard()
            is DailyFlipEvent.OnCardFlipped -> handleCardFlipped()
            is DailyFlipEvent.OnCardMarkedAsDone -> handleCardMarkedAsDone()
        }
    }

    private fun handleCategorySelection(category: PleasureCategory) = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyFlipScreenState.Ready) {
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(selectedCategory = category)
            )
        }
    }

    private fun handleDrawCard() = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyFlipScreenState.Ready && currentState.dailyPleasure == null) {
            val randomPleasure = getRandomPleasureUseCase(currentState.selectedCategory).first()
            saveHistoryEntryUseCase(randomPleasure)
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(dailyPleasure = randomPleasure)
            )
        }
    }

    private fun handleCardFlipped() = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyFlipScreenState.Ready) {
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(isCardFlipped = true),
                headerMessage = "Votre plaisir du jour"
            )
        }
    }

    private fun handleCardMarkedAsDone() = viewModelScope.launch {
        val currentState = _uiState.value.screenState
        if (currentState is DailyFlipScreenState.Ready) {
            currentState.dailyPleasure?.let {
                saveHistoryEntryUseCase(
                    pleasure = currentState.dailyPleasure,
                    markAsCompleted = true
                )
                _uiState.value = _uiState.value.copy(
                    screenState = DailyFlipScreenState.Completed,
                    ""
                )
            }
        }
    }
}
