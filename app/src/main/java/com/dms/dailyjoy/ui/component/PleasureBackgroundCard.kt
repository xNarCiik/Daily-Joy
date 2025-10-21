package com.dms.dailyjoy.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun PleasureBackgroundCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Grande forme décorative en haut à gauche
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopStart)
                .offset(x = (-80).dp, y = (-60).dp)
                .graphicsLayer {
                    rotationZ = 25f
                    alpha = 0.4f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                            Color.Transparent
                        ),
                        radius = 300f
                    ),
                    shape = RoundedCornerShape(60.dp)
                )
        )

        // Forme décorative en bas à droite
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 70.dp, y = 80.dp)
                .graphicsLayer {
                    rotationZ = -20f
                    alpha = 0.35f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                            Color.Transparent
                        ),
                        radius = 350f
                    ),
                    shape = RoundedCornerShape(70.dp)
                )
        )

        // Forme abstraite au centre (subtile)
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
                .offset(y = 30.dp)
                .graphicsLayer {
                    alpha = 0.15f
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            Color.Transparent
                        ),
                        radius = 400f
                    ),
                    shape = CircleShape
                )
        )
    }
}

@LightDarkPreview
@Composable
private fun PleasureBackCardPreview() {
    DailyJoyTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            PleasureBackgroundCard()
        }
    }
}
