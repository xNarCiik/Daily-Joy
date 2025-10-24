package com.dms.dailyjoy.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.ui.dailypleasure.component.CategoryChip
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasure
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PleasureCard(
    modifier: Modifier = Modifier,
    pleasure: Pleasure,
    durationRotation: Int,
    onCardFlipped: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var isFlipped by remember { mutableStateOf(pleasure.isFlipped) }
    var isJumping by remember { mutableStateOf(false) }
    var shouldAnimate by remember { mutableStateOf(false) }

    LaunchedEffect(pleasure) {
        if (isFlipped != pleasure.isFlipped) {
            shouldAnimate = false
            isFlipped = pleasure.isFlipped
        }
    }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = if (shouldAnimate) {
            tween(
                durationMillis = durationRotation,
                easing = { it * it * (3 - 2 * it) }
            )
        } else {
            tween(durationMillis = 0)
        },
        label = "rotationAnimation",
        finishedListener = { finalValue ->
            if (finalValue == 180f && !pleasure.isFlipped) {
                onCardFlipped()
            }
            shouldAnimate = false
        }
    )

    val jumpScale by animateFloatAsState(
        targetValue = if (isJumping) 1.15f else 1f,
        animationSpec = tween(2000, easing = { overshootEasing(t = it) }),
        label = "scaleAnimation"
    )

    val jumpOffset by animateFloatAsState(
        targetValue = if (isJumping) (-8f) else 0f,
        animationSpec = tween(2000, easing = { overshootEasing(t = it) }),
        label = "offsetAnimation"
    )

    Card(
        modifier = modifier
            .width(300.dp)
            .height(440.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
                scaleX = jumpScale
                scaleY = jumpScale
                translationY = jumpOffset
            },
        shape = RoundedCornerShape(size = 32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            if (!isFlipped && !isJumping) {
                scope.launch {
                    isJumping = true
                    delay(250)
                    shouldAnimate = true
                    isFlipped = true
                    delay(100)
                    isJumping = false
                }
            }
        }
    ) {
        if (rotation < 90f) {
            PleasureBackCard()
        } else {
            PleasureCardContent(
                modifier = Modifier.graphicsLayer { rotationY = 180f },
                pleasure = pleasure
            )
        }
    }
}

private fun overshootEasing(t: Float, tension: Float = 3f) =
    (t - 1).let { it * it * ((tension + 1) * it + tension) + 1 }

@Composable
private fun PleasureBackCard() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6366F1), // TODO EXPORT THEME ?
                        Color(0xFF8B5CF6),
                        Color(0xFFEC4899)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon with light effect
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text( // TODO STRINGS
                text = "✨ Touchez la carte ✨",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "pour découvrir votre plaisir",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Animation dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.6f))
                    )
                }
            }
        }
    }
}

@Composable
private fun PleasureCardContent(modifier: Modifier = Modifier, pleasure: Pleasure) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                    )
                )
            )
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val category = pleasure.category

            CategoryChip(category = category, isSelected = true, onClick = {})

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎉",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Title
            Text(
                text = pleasure.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Description
            Text(
                text = pleasure.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 24.sp
            )

            Spacer(Modifier.weight(1f))

            // Bottom section with card number
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Decorative dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == 2) 10.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(
                                        alpha = if (index == 2) 0.6f else 0.3f
                                    )
                                )
                        )
                    }
                }

                // Card number
                Text(
                    text = "Plaisir #${pleasure.id}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun NotFlippedPleasureCardPreview() {
    DailyJoyTheme {
        PleasureCard(
            pleasure = previewDailyPleasure.copy(isFlipped = false),
            durationRotation = 0,
            onCardFlipped = {}
        )
    }
}

@LightDarkPreview
@Composable
private fun FlippedPleasureCardPreview() {
    DailyJoyTheme {
        PleasureCard(
            pleasure = previewDailyPleasure,
            durationRotation = 0,
            onCardFlipped = {}
        )
    }
}
