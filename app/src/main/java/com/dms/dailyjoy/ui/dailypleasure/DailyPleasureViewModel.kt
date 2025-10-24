package com.dms.dailyjoy.ui.dailypleasure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import com.dms.dailyjoy.domain.usecase.dailypleasure.DrawDailyPleasureUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetPleasuresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val MinimumPleasuresCount = 7

@HiltViewModel
class DailyPleasureViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val drawDailyPleasureUseCase: DrawDailyPleasureUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyPleasureUiState())
    val uiState: StateFlow<DailyPleasureUiState> = _uiState.asStateFlow()

    init {
        initializeScreen()
    }

    private fun initializeScreen() {
        getPleasuresUseCase().onEach { pleasuresList ->
            val enabledPleasures = pleasuresList.count { it.isEnabled }
            if (enabledPleasures < MinimumPleasuresCount) {
                _uiState.value =
                    DailyPleasureUiState(
                        screenState = DailyPleasureScreenState.SetupRequired(
                            pleasureCount = enabledPleasures
                        ),// TODO STRING
                        headerMessage = "Avant de tirer votre carte, vous devez ajouter au moins $MinimumPleasuresCount plaisirs"
                    )
            } else {
                loadReadyState()
            }
        }.launchIn(viewModelScope)
    }

    private fun loadReadyState() = viewModelScope.launch {
        _uiState.value = DailyPleasureUiState(
            screenState = DailyPleasureScreenState.Ready(
                availableCategories = PleasureCategory.entries
            ),
            headerMessage = getRandomDailyMessageUseCase()
        )
    }

    fun onEvent(event: DailyPleasureEvent) {
        when (event) {
            is DailyPleasureEvent.OnCategorySelected -> handleCategorySelection(event.category)
            is DailyPleasureEvent.OnDrawCardClicked -> handleDrawCard()
            is DailyPleasureEvent.OnCardFlipped -> handleCardFlipped()
            is DailyPleasureEvent.OnCardMarkedAsDone -> handleCardMarkedAsDone()
        }
    }

    private fun handleCategorySelection(category: PleasureCategory) {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready) {
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(selectedCategory = category)
            )
        }
    }

    private fun handleDrawCard() {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready && currentState.drawnPleasure == null) {
            drawDailyPleasureUseCase(currentState.selectedCategory).onEach { pleasure ->
                _uiState.value = _uiState.value.copy(
                    screenState = currentState.copy(drawnPleasure = pleasure)
                )
            }.launchIn(viewModelScope)
        }
    }

    private fun handleCardFlipped() {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready) {
            _uiState.value = _uiState.value.copy(
                screenState = currentState.copy(isCardFlipped = true)
            )
        }
    }

    private fun handleCardMarkedAsDone() {
        val currentState = _uiState.value.screenState
        if (currentState is DailyPleasureScreenState.Ready && currentState.drawnPleasure != null) {
            _uiState.value = _uiState.value.copy(
                screenState = DailyPleasureScreenState.Completed
            )
        }
    }
}
