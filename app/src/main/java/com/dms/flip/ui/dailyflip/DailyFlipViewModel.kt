package com.dms.flip.ui.dailyflip

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.R
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MinimumPleasuresCount = 7

@HiltViewModel
class DailyFlipViewModel @Inject constructor(
    private val resources: Resources,
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val getRandomPleasureUseCase: GetRandomPleasureUseCase,
    private val saveHistoryEntryUseCase: SaveHistoryEntryUseCase,
    private val getTodayHistoryEntryUseCase: GetTodayHistoryEntryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyFlipUiState())
    val uiState: StateFlow<DailyFlipUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(screenState = DailyFlipScreenState.Loading)

            combine(
                getPleasuresUseCase(),
                getTodayHistoryEntryUseCase(),
                flow { emit(getRandomDailyMessageUseCase()) }
            ) { pleasures, todayHistory, randomMessage ->

                val enabledCount = pleasures.count { it.isEnabled }
                val isSetupRequired = enabledCount < MinimumPleasuresCount

                when {
                    isSetupRequired -> DailyFlipUiState(
                        screenState = DailyFlipScreenState.SetupRequired(enabledCount)
                    )

                    todayHistory?.completed == true -> DailyFlipUiState(
                        screenState = DailyFlipScreenState.Completed,
                        headerMessage = ""
                    )

                    else -> DailyFlipUiState(
                        screenState = DailyFlipScreenState.Ready(
                            availableCategories = PleasureCategory.entries,
                            dailyPleasure = todayHistory?.toPleasure(), // TODO LET HISTORY
                            isCardFlipped = todayHistory != null
                        ),
                        headerMessage = if (todayHistory == null)
                            randomMessage
                        else
                            resources.getString(R.string.your_flip_daily)
                    )
                }
            }
                .catch { e ->
                    _uiState.value = DailyFlipUiState(
                        screenState = DailyFlipScreenState.Error(
                            e.message ?: resources.getString(R.string.generic_error_message)
                        )
                    )
                }
                .collectLatest { newState ->
                    _uiState.value = newState
                }
        }
    }

    fun onEvent(event: DailyFlipEvent) {
        when (event) {
            is DailyFlipEvent.Reload -> observeData()
            is DailyFlipEvent.OnCategorySelected -> handleCategorySelection(event.category)
            is DailyFlipEvent.OnCardClicked -> handleDrawCard()
            is DailyFlipEvent.OnCardFlipped -> handleCardFlipped()
            is DailyFlipEvent.OnCardMarkedAsDone -> handleCardMarkedAsDone()
        }
    }

    private fun handleCategorySelection(category: PleasureCategory) {
        val current = _uiState.value.screenState
        if (current is DailyFlipScreenState.Ready) {
            _uiState.update {
                it.copy(screenState = current.copy(selectedCategory = category))
            }
        }
    }

    private fun handleDrawCard() = viewModelScope.launch {
        val current = _uiState.value.screenState
        if (current is DailyFlipScreenState.Ready && current.dailyPleasure == null) {
            try {
                val randomPleasure = getRandomPleasureUseCase(current.selectedCategory).first()
                saveHistoryEntryUseCase(randomPleasure)
                _uiState.update {
                    it.copy(screenState = current.copy(dailyPleasure = randomPleasure))
                }
            } catch (e: Exception) {
                _uiState.value = DailyFlipUiState(
                    screenState = DailyFlipScreenState.Error(
                        "Impossible de tirer une carte : ${e.message}"
                    )
                )
            }
        }
    }

    private fun handleCardFlipped() {
        val current = _uiState.value.screenState
        if (current is DailyFlipScreenState.Ready) {
            _uiState.update {
                it.copy(
                    screenState = current.copy(isCardFlipped = true),
                    headerMessage = resources.getString(R.string.your_flip_daily)
                )
            }
        }
    }

    private fun handleCardMarkedAsDone() = viewModelScope.launch {
        val current = _uiState.value.screenState
        if (current is DailyFlipScreenState.Ready) {
            try {
                current.dailyPleasure?.let { pleasure ->
                    saveHistoryEntryUseCase(pleasure, markAsCompleted = true)
                    _uiState.value = DailyFlipUiState(
                        screenState = DailyFlipScreenState.Completed,
                        headerMessage = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.value = DailyFlipUiState(
                    screenState = DailyFlipScreenState.Error(
                        "Erreur lors de la validation du plaisir : ${e.message}"
                    )
                )
            }
        }
    }
}
