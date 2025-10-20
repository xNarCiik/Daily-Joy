package com.dms.dailyjoy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.usecase.GetPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomPleasureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DailyPleasureState(
    val dailyMessage: String = "",
    val dailyPleasure: Pleasure = Pleasure(),
    val waitDonePleasure: Boolean = false
)

@HiltViewModel
class PleasureViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val getRandomPleasureUseCase: GetRandomPleasureUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyPleasureState())
    val state = _state.asStateFlow()

    init {
        resetState()
    }

    fun onDailyCardFlipped() = viewModelScope.launch {
        _state.update {
            it.copy(
                dailyPleasure = it.dailyPleasure.copy(isFlipped = true),
                waitDonePleasure = true
            )
        }
    }

    fun markDailyCardAsDone() = viewModelScope.launch {
        _state.update {
            it.copy(dailyPleasure = it.dailyPleasure.copy(isDone = true), waitDonePleasure = false)
        }
    }

    fun resetState() = viewModelScope.launch {
        _state.update {
            DailyPleasureState(
                dailyMessage = getRandomDailyMessage(),
                dailyPleasure = getRandomPleasure()
            )
        }
    }

    private fun getRandomDailyMessage(): String = getRandomDailyMessageUseCase.invoke()

    private suspend fun getRandomPleasure(): Pleasure = getRandomPleasureUseCase.invoke().firstOrNull() ?: Pleasure()
}
