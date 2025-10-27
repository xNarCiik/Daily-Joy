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
import androidx.compose.ui.graphics.Brush
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
import com.dms.dailyjoy.ui.util.LightDarkPreview
import kotlinx.coroutines.delay

@Composable
fun DailyPleasureCompletedContent() {
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
            delay(300)
            playAnimation = true
        }
    }

    LaunchedEffect(successProgress) {
        if (successProgress == 1f) {
            hasPlayed = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                    )
                )
            )
            .padding(vertical = 48.dp, horizontal = 24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.pleasure_completed_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    modifier = Modifier.fillMaxSize(),
                    composition = successComposition,
                    progress = { if(hasPlayed) 1f else successProgress }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.pleasure_completed_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun DailyPleasureCompletedContentPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureCompletedContent()
        }
    }
}
