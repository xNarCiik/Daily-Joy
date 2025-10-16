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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PleasureViewModel @Inject constructor(
    private val getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase,
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val getRandomPleasureUseCase: GetRandomPleasureUseCase
) : ViewModel() {

    private val _dailyMessage = MutableStateFlow("")
    val dailyMessage = _dailyMessage.asStateFlow()

    private val _dailyPleasure = MutableStateFlow<Pleasure?>(null)
    val dailyPleasure = _dailyPleasure.asStateFlow()

    private val _pleasures = MutableStateFlow<List<Pleasure>>(listOf())
    val pleasures = _pleasures.asStateFlow()

    init {
        viewModelScope.launch {
            _dailyMessage.emit(value = getRandomDailyMessage())
            _dailyPleasure.emit(value = getRandomPleasure())
            _pleasures.emit(value = getPleasures())
        }
    }

    fun getRandomDailyMessage(): String = getRandomDailyMessageUseCase.invoke()
    fun getPleasures(): List<Pleasure> = getPleasuresUseCase.invoke()

    fun getRandomPleasure(): Pleasure = getRandomPleasureUseCase.invoke()

    fun flipDailyCard() = viewModelScope.launch {
        _dailyPleasure.emit(value = _dailyPleasure.value?.copy(isFlipped = true))
    }
}