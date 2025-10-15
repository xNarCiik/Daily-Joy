package com.dms.dailyjoy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.usecase.GetPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomPleasureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PleasureViewModel @Inject constructor(
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val getRandomPleasureUseCase: GetRandomPleasureUseCase
) : ViewModel() {

    private val _pleasures = MutableStateFlow<List<Pleasure>>(listOf())
    val pleasures = _pleasures.asStateFlow()

    init {
        viewModelScope.launch {
            _pleasures.emit(value = getPleasures())
        }
    }

    fun getPleasures(): List<Pleasure> = getPleasuresUseCase.invoke()

    fun getRandomPleasure(): Pleasure = getRandomPleasureUseCase.invoke()
}