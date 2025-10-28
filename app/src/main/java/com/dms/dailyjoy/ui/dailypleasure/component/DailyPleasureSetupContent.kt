package com.dms.dailyjoy.ui.dailypleasure.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

@Composable
fun DailyPleasureSetupContent(
    modifier: Modifier = Modifier,
    currentPleasureCount: Int,
    requiredCount: Int = 7,
    onConfigureClick: () -> Unit,
) {
    val gradients = dailyJoyGradients()
    var playAnimation by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(gradients.card)
                .padding(vertical = 32.dp, horizontal = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Animation Lottie
                val plantComposition by rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(resId = R.raw.plant_growing)
                )
                val plantProgress by animateLottieCompositionAsState(
                    composition = plantComposition,
                    isPlaying = playAnimation,
                    restartOnPlay = false
                )

                LaunchedEffect(plantProgress) {
                    if (plantProgress == 1f) {
                        playAnimation = false
                    }
                }

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(140.dp),
                        composition = plantComposition,
                        progress = { if (playAnimation) plantProgress else 1.0f }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Titre
                Text(
                    text = stringResource(R.string.setup_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                // Sous-titre
                Text(
                    text = stringResource(
                        R.string.setup_subtitle,
                        currentPleasureCount,
                        requiredCount
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Indicateur de progression
                ProgressIndicator(
                    current = currentPleasureCount,
                    required = requiredCount
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Bouton d'action
                Button(
                    onClick = onConfigureClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = stringResource(R.string.setup_button_text),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    current: Int,
    required: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(required) { index ->
            val isCompleted = index < current
            Box(
                modifier = Modifier
                    .size(if (isCompleted) 14.dp else 12.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) {
                            Color.White
                        } else {
                            Color.White.copy(alpha = 0.3f)
                        }
                    )
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun SetupContentPreview() {
    DailyJoyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DailyPleasureSetupContent(
                currentPleasureCount = 4,
                requiredCount = 7,
                onConfigureClick = {}
            )
        }
    }
}
