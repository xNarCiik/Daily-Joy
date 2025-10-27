package com.dms.dailyjoy.ui.onboarding.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.onboarding.OnboardingStep

@Composable
fun OnboardingProgressBar(
    currentStep: OnboardingStep,
    totalSteps: Int // TODO REMOVE ? OR UPDATE UI
) {
    val progress = when (currentStep) {
        OnboardingStep.WELCOME -> 0f
        OnboardingStep.USERNAME -> 0.25f
        OnboardingStep.PLEASURES -> 0.5f
        OnboardingStep.NOTIFICATIONS -> 0.75f
        OnboardingStep.COMPLETE -> 1f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "progress"
    )

    Column {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            drawStopIndicator = {}
        )
    }
}
