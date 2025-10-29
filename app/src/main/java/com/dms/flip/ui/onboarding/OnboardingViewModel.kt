package com.dms.flip.ui.onboarding

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.data.model.Pleasure
import com.dms.flip.domain.usecase.onboarding.SaveOnboardingStatusUseCase
import com.dms.flip.domain.usecase.pleasures.GetPleasuresUseCase
import com.dms.flip.notification.DailyReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    application: Application,
    private val saveOnboardingStatusUseCase: SaveOnboardingStatusUseCase,
    private val getPleasuresUseCase: GetPleasuresUseCase
) : ViewModel() {
    private val dailyReminderManager = DailyReminderManager(application)

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        loadPleasures()
    }

    fun onStartClick() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(showWelcome = false)
    }

    fun nextStep() {
        viewModelScope.launch {
            val currentStep = _uiState.value.currentStep

            // Navigation normale pour les autres steps
            val nextStep = when (currentStep) {
                OnboardingStep.USERNAME -> OnboardingStep.PLEASURES
                OnboardingStep.PLEASURES -> OnboardingStep.NOTIFICATIONS
                OnboardingStep.NOTIFICATIONS -> {
                    if (!_uiState.value.notificationEnabled && !_uiState.value.hasShownNotificationWarning) {
                        // Afficher l'alerte une seule fois
                        _uiState.value = _uiState.value.copy(
                            showNotificationSkipWarning = true,
                            hasShownNotificationWarning = true
                        )
                        return@launch
                    } else if (_uiState.value.notificationEnabled) {
                        // Si activé, aller vers REMINDER_TIME
                        _uiState.value =
                            _uiState.value.copy(currentStep = OnboardingStep.REMINDER_TIME)
                        return@launch
                    } else {
                        // Si refusé et warning déjà montré, terminer directement
                        completeOnboarding()
                        return@launch
                    }
                }

                OnboardingStep.REMINDER_TIME -> {
                    // Terminer après REMINDER_TIME
                    completeOnboarding()
                    return@launch
                }
            }
            _uiState.value = _uiState.value.copy(currentStep = nextStep)
        }
    }

    fun previousStep() {
        viewModelScope.launch {
            val currentStep = _uiState.value.currentStep
            val previousStep = when (currentStep) {
                OnboardingStep.USERNAME -> OnboardingStep.USERNAME
                OnboardingStep.PLEASURES -> OnboardingStep.USERNAME
                OnboardingStep.NOTIFICATIONS -> OnboardingStep.PLEASURES
                OnboardingStep.REMINDER_TIME -> OnboardingStep.NOTIFICATIONS
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

    fun dismissNotificationWarning() {
        _uiState.value = _uiState.value.copy(showNotificationSkipWarning = false)
    }

    fun completeOnboarding() = viewModelScope.launch {
        val pleasures = _uiState.value.availablePleasures.filter { it.isEnabled }
        val hasNotificationsEnabled = _uiState.value.notificationEnabled
        val reminderTime = _uiState.value.reminderTime

        if (hasNotificationsEnabled) {
            dailyReminderManager.schedule(reminderTime)
        }

        saveOnboardingStatusUseCase(
            username = _uiState.value.username,
            pleasures = pleasures,
            notificationEnabled = hasNotificationsEnabled,
            reminderTime = reminderTime
        )
        _uiState.value = _uiState.value.copy(completed = true)
    }

    private fun loadPleasures() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(availablePleasures = getPleasuresUseCase().first())
    }
}
