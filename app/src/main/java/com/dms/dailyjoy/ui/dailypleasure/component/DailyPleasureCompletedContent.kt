package com.dms.dailyjoy.ui.dailypleasure.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.theme.dailyJoyGradients
import com.dms.dailyjoy.ui.util.LightDarkPreview
import kotlinx.coroutines.delay

@Composable
fun DailyPleasureCompletedContent(
    modifier: Modifier = Modifier
) {
    val gradients = dailyJoyGradients()
    var hasPlayed by rememberSaveable { mutableStateOf(false) }
    var playAnimation by remember { mutableStateOf(false) }

    val successComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.checkmark)
    )

    val successProgress by animateLottieCompositionAsState(
        composition = successComposition,
        isPlaying = playAnimation,
        restartOnPlay = false,
        speed = 0.6f
    )

    LaunchedEffect(Unit) {
        if (!hasPlayed) {
            delay(400)
            playAnimation = true
        }
    }

    LaunchedEffect(successProgress) {
        if (successProgress == 1f) {
            hasPlayed = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(gradients.card)
                .padding(vertical = 48.dp, horizontal = 32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(140.dp),
                        composition = successComposition,
                        progress = { if (hasPlayed) 1f else successProgress }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Titre
                Text(
                    text = stringResource(id = R.string.pleasure_completed_title),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Sous-titre
                Text(
                    text = stringResource(id = R.string.pleasure_completed_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun DailyPleasureCompletedContentPreview() {
    DailyJoyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DailyPleasureCompletedContent()
        }
    }
}
