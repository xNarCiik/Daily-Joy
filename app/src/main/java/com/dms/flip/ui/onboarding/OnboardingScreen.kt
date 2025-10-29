package com.dms.flip.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dms.flip.ui.onboarding.component.OnboardingNavigation
import com.dms.flip.ui.onboarding.component.OnboardingProgressBar
import com.dms.flip.ui.onboarding.component.step.NotificationPermissionStep
import com.dms.flip.ui.onboarding.component.step.PleasuresStep
import com.dms.flip.ui.onboarding.component.step.ReminderTimeStep
import com.dms.flip.ui.onboarding.component.step.UsernameStep
import com.dms.flip.ui.onboarding.component.step.WelcomeScreen

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AnimatedContent(
        targetState = uiState.showWelcome,
        transitionSpec = {
            slideInHorizontally { fullWidth -> fullWidth } + fadeIn() togetherWith
                    slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
        },
        label = "Onboarding transition"
    ) { showWelcome ->
        when (showWelcome) {
            true -> {
                WelcomeScreen(
                    modifier = modifier,
                    onStartClick = viewModel::onStartClick
                )
            }

            else -> {
                OnboardingContent(modifier = modifier, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun OnboardingContent(modifier: Modifier, viewModel: OnboardingViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        OnboardingProgressBar(
            currentStep = uiState.currentStep,
            totalSteps = 4
        )
        var isForward by remember { mutableStateOf(true) }

        // Détecter la direction de navigation
        val currentStepOrder = uiState.currentStep.ordinal
        var previousStep by remember { mutableStateOf(OnboardingStep.USERNAME) }
        val previousStepOrder = previousStep.ordinal

        if (currentStepOrder != previousStepOrder) {
            isForward = currentStepOrder > previousStepOrder
            previousStep = uiState.currentStep
        }

        // Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = uiState.currentStep,
                transitionSpec = {
                    val slideDirection = if (isForward) 1 else -1

                    (slideInHorizontally(
                        initialOffsetX = { it * slideDirection },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn(
                        animationSpec = tween(300)
                    )) togetherWith (slideOutHorizontally(
                        targetOffsetX = { -it * slideDirection },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeOut(
                        animationSpec = tween(300)
                    )) using SizeTransform(clip = false)
                },
                label = "onboardingContent"
            ) { step ->
                when (step) {
                    OnboardingStep.USERNAME -> UsernameStep(
                        username = uiState.username,
                        onUsernameChange = viewModel::updateUsername
                    )

                    OnboardingStep.PLEASURES -> PleasuresStep(
                        pleasures = uiState.availablePleasures,
                        onTogglePleasure = viewModel::togglePleasure
                    )

                    OnboardingStep.NOTIFICATIONS -> NotificationPermissionStep(
                        notificationEnabled = uiState.notificationEnabled,
                        onNotificationToggle = viewModel::updateNotificationEnabled,
                        showSkipWarning = uiState.showNotificationSkipWarning,
                        onDismissWarning = viewModel::dismissNotificationWarning
                    )

                    OnboardingStep.REMINDER_TIME -> ReminderTimeStep(
                        reminderTime = uiState.reminderTime,
                        onTimeChange = viewModel::updateReminderTime
                    )

                    else -> {}
                }
            }
        }

        // Navigation buttons (cachés sur WELCOME)
        OnboardingNavigation(
            currentStep = uiState.currentStep,
            canGoNext = canGoNext(uiState = uiState),
            onPrevious = viewModel::previousStep,
            onNext = viewModel::nextStep
        )
    }
}

private fun canGoNext(uiState: OnboardingUiState): Boolean {
    return when (uiState.currentStep) {
        OnboardingStep.USERNAME -> uiState.username.isNotBlank()
        OnboardingStep.PLEASURES -> uiState.availablePleasures.any { it.isEnabled }
        OnboardingStep.NOTIFICATIONS -> true
        OnboardingStep.REMINDER_TIME -> true
    }
}
