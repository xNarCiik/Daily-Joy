package com.dms.flip.ui.onboarding

import com.dms.flip.data.model.Pleasure

data class OnboardingUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentStep: OnboardingStep = OnboardingStep.WELCOME,
    val username: String = "",
    val availablePleasures: List<Pleasure> = emptyList(),
    val notificationEnabled: Boolean = false,
    val reminderTime: String = "11:00"
)

enum class OnboardingStep {
    WELCOME,
    USERNAME,
    PLEASURES,
    NOTIFICATIONS,
    COMPLETE
}
