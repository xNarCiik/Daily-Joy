package com.dms.dailyjoy.ui.dailypleasure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun DailyPleasureScreen() {
    val viewModel: PleasureViewModel = hiltViewModel()
    val dailyMessage by viewModel.dailyMessage.collectAsState()
    val dailyPleasure by viewModel.dailyPleasure.collectAsState()

    dailyPleasure?.let { pleasure ->
        var contentVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            contentVisible = true
        }

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 2000))
        ) {
            DailyPleasureContent(
                dailyMessage = dailyMessage,
                dailyPleasure = pleasure,
                flipCard = { viewModel.flipDailyCard() }
            )
        }
    }
}

@Composable
private fun DailyPleasureContent(
    dailyMessage: String,
    dailyPleasure: Pleasure,
    flipCard: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 24.dp)
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

            DailyPleasureCard(pleasure = dailyPleasure, flipCard = flipCard)

            Spacer(Modifier.weight(1f))
        }

        // Animation Confetti
        val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(resId = R.raw.confetti))
        val lottieProgress by animateLottieCompositionAsState(
            composition = lottieComposition,
            isPlaying = dailyPleasure.isFlipped,
            restartOnPlay = false
        )

        if (dailyPleasure.isFlipped) {
            LottieAnimation(
                modifier = Modifier.fillMaxSize(),
                composition = lottieComposition,
                progress = { lottieProgress }
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