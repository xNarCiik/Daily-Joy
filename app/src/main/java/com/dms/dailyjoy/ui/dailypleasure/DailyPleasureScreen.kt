package com.dms.dailyjoy.ui.dailypleasure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.ui.PleasureViewModel
import com.dms.dailyjoy.ui.component.DailyPleasureCard
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasure
import com.dms.dailyjoy.ui.util.rotationCardAnimationDuration

@Composable
fun DailyPleasureScreen() {
    val viewModel: PleasureViewModel = hiltViewModel()
    val dailyMessage by viewModel.dailyMessage.collectAsState()
    val dailyPleasure by viewModel.dailyPleasure.collectAsState()

    dailyPleasure?.let { pleasure ->
        DailyPleasureContent(
            dailyMessage = dailyMessage,
            dailyPleasure = pleasure,
            flipCard = { viewModel.flipDailyCard() }
        )
    }
}

@Composable
private fun DailyPleasureContent(
    dailyMessage: String,
    dailyPleasure: Pleasure,
    flipCard: () -> Unit
) {
    var cardIsFlipped by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            Text(
                text = dailyMessage,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))

            DailyPleasureCard(
                pleasure = dailyPleasure,
                durationRotation = rotationCardAnimationDuration,
                flipCard = flipCard,
                onCardFlipped = { cardIsFlipped = true }
            )

            Spacer(Modifier.weight(1f))

            InfoText(cardIsFlipped = cardIsFlipped)

            Spacer(Modifier.weight(1f))
        }

        // Animation Confetti
        val confettiComposition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                resId = R.raw.confetti
            )
        )
        val confettiProgress by animateLottieCompositionAsState(
            composition = confettiComposition,
            isPlaying = cardIsFlipped,
            restartOnPlay = false
        )

        if (cardIsFlipped) {
            LottieAnimation(
                composition = confettiComposition,
                progress = { confettiProgress }
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureContentPreview() {
    DailyJoyTheme {
        DailyPleasureContent(
            dailyMessage = "Et si aujourd'hui, on prenait le temps de...",
            dailyPleasure = previewDailyPleasure,
            flipCard = {}
        )
    }
}