package com.dms.flip.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.data.model.Pleasure
import com.dms.flip.domain.usecase.onboarding.SaveOnboardingStatusUseCase
import com.dms.flip.domain.usecase.pleasures.GetPleasuresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val saveOnboardingStatusUseCase: SaveOnboardingStatusUseCase,
    private val getPleasuresUseCase: GetPleasuresUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        loadPleasures()
    }

    fun nextStep() {
        viewModelScope.launch {
            val currentStep = _uiState.value.currentStep
            val nextStep = when (currentStep) {
                OnboardingStep.WELCOME -> OnboardingStep.USERNAME
                OnboardingStep.USERNAME -> OnboardingStep.PLEASURES
                OnboardingStep.PLEASURES -> OnboardingStep.NOTIFICATIONS
                OnboardingStep.NOTIFICATIONS -> OnboardingStep.COMPLETE
                OnboardingStep.COMPLETE -> OnboardingStep.COMPLETE
            }
            _uiState.value = _uiState.value.copy(currentStep = nextStep)
        }
    }

    fun previousStep() {
        viewModelScope.launch {
            val currentStep = _uiState.value.currentStep
            val previousStep = when (currentStep) {
                OnboardingStep.WELCOME -> OnboardingStep.WELCOME
                OnboardingStep.USERNAME -> OnboardingStep.WELCOME
                OnboardingStep.PLEASURES -> OnboardingStep.USERNAME
                OnboardingStep.NOTIFICATIONS -> OnboardingStep.PLEASURES
                OnboardingStep.COMPLETE -> OnboardingStep.NOTIFICATIONS
            }
            _uiState.value = _uiState.value.copy(currentStep = previousStep)
        }
    }

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun togglePleasure(pleasure: Pleasure) {
        val updatedPleasures = _uiState.value.availablePleasures.map {
            if (it.id == pleasure.id) it.copy(isEnabled = !it.isEnabled)
            else it
        }
        _uiState.value = _uiState.value.copy(availablePleasures = updatedPleasures)
    }

    fun updateNotificationEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationEnabled = enabled)
    }

    fun updateReminderTime(time: String) {
        _uiState.value = _uiState.value.copy(reminderTime = time)
    }

    fun completeOnboarding() = viewModelScope.launch {
        // TODO: Sauvegarder les préférences (pleasures, notifications)
        saveOnboardingStatusUseCase(username = _uiState.value.username)
        _uiState.value = _uiState.value.copy(username = _uiState.value.username, currentStep = OnboardingStep.COMPLETE)
    }

    private fun loadPleasures() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(availablePleasures = getPleasuresUseCase().first())
    }
}
