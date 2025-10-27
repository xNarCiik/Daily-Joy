package com.dms.dailyjoy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.dailyjoy.domain.model.RootNavigationState
import com.dms.dailyjoy.domain.usecase.navigation.GetRootNavigationStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getRootNavigationStateUseCase: GetRootNavigationStateUseCase
) : ViewModel() {
    val rootNavigationState: StateFlow<RootNavigationState> = getRootNavigationStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RootNavigationState.Loading
        )
}