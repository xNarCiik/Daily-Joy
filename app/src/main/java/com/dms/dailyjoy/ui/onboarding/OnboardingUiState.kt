package com.dms.dailyjoy.ui.onboarding

import com.dms.dailyjoy.data.model.Pleasure

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.WELCOME,
    val username: String = "",
    val availablePleasures: List<Pleasure> = emptyList(),
    val notificationEnabled: Boolean = false,
    val reminderTime: String = "08:00",
    val isCompleted: Boolean = false
)

enum class OnboardingStep {
    WELCOME,
    USERNAME,
    PLEASURES,
    NOTIFICATIONS,
    COMPLETE
}
