package com.dms.dailyjoy.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dms.dailyjoy.ui.onboarding.component.OnboardingNavigation
import com.dms.dailyjoy.ui.onboarding.component.OnboardingProgressBar
import com.dms.dailyjoy.ui.onboarding.component.step.NotificationStep
import com.dms.dailyjoy.ui.onboarding.component.step.PleasuresStep
import com.dms.dailyjoy.ui.onboarding.component.step.UsernameStep
import com.dms.dailyjoy.ui.onboarding.component.step.WelcomeStep

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isCompleted) {
        onComplete()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        // Progress bar
        OnboardingProgressBar(
            currentStep = uiState.currentStep,
            totalSteps = OnboardingStep.entries.size - 1 // Exclude COMPLETE
        )

        // Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = uiState.currentStep,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) togetherWith slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                },
                label = "onboardingContent"
            ) { step ->
                when (step) {
                    OnboardingStep.WELCOME -> WelcomeStep()
                    OnboardingStep.USERNAME -> UsernameStep(
                        username = uiState.username,
                        onUsernameChange = viewModel::updateUsername
                    )

                    OnboardingStep.PLEASURES -> PleasuresStep(
                        pleasures = uiState.availablePleasures,
                        onTogglePleasure = viewModel::togglePleasure
                    )

                    OnboardingStep.NOTIFICATIONS -> NotificationStep(
                        notificationEnabled = uiState.notificationEnabled,
                        reminderTime = uiState.reminderTime,
                        onNotificationToggle = viewModel::updateNotificationEnabled,
                        onTimeChange = viewModel::updateReminderTime
                    )

                    OnboardingStep.COMPLETE -> Unit
                }
            }
        }

        // Navigation buttons
        OnboardingNavigation(
            currentStep = uiState.currentStep,
            canGoNext = canGoNext(uiState = uiState),
            onPrevious = viewModel::previousStep,
            onNext = {
                if (uiState.currentStep == OnboardingStep.NOTIFICATIONS) {
                    viewModel.completeOnboarding()
                } else {
                    viewModel.nextStep()
                }
            }
        )
    }
}

private fun canGoNext(uiState: OnboardingUiState): Boolean {
    return when (uiState.currentStep) {
        OnboardingStep.WELCOME -> true
        OnboardingStep.USERNAME -> uiState.username.isNotBlank()
        OnboardingStep.PLEASURES -> uiState.availablePleasures.any { it.isEnabled }
        OnboardingStep.NOTIFICATIONS -> true
        OnboardingStep.COMPLETE -> false
    }
}
