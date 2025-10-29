package com.dms.flip.ui.onboarding

import com.dms.flip.data.model.Pleasure

data class OnboardingUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val showWelcome: Boolean = true,
    val currentStep: OnboardingStep = OnboardingStep.USERNAME,
    val username: String = "",
    val availablePleasures: List<Pleasure> = emptyList(),
    val notificationEnabled: Boolean = false,
    val reminderTime: String = "09:00",
    val showNotificationSkipWarning: Boolean = false,
    val hasShownNotificationWarning: Boolean = false,
    val completed: Boolean = false
)

enum class OnboardingStep {
    USERNAME,
    PLEASURES,
    NOTIFICATIONS,
    REMINDER_TIME
}
