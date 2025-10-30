package com.dms.flip.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.domain.model.RootNavigationState
import com.dms.flip.domain.usecase.navigation.GetRootNavigationStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRootNavigationStateUseCase: GetRootNavigationStateUseCase
) : ViewModel() {

    private val _rootNavigationState =
        MutableStateFlow<RootNavigationState>(RootNavigationState.Loading)
    val rootNavigationState: StateFlow<RootNavigationState> = _rootNavigationState

    init {
        observeRootState()
    }

    private fun observeRootState() {
        getRootNavigationStateUseCase()
            .onEach { _rootNavigationState.value = it }
            .launchIn(viewModelScope)
    }
}
