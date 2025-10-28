package com.dms.flip.ui.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dms.flip.R
import com.dms.flip.data.model.Pleasure
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.theme.flipGradients
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewDailyPleasure
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PleasureCard(
    modifier: Modifier = Modifier,
    pleasure: Pleasure?,
    flipped: Boolean,
    durationRotation: Int,
    onCardFlipped: () -> Unit,
    onClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var isFlipped by remember { mutableStateOf(flipped) }
    var isJumping by remember { mutableStateOf(false) }
    var shouldAnimate by remember { mutableStateOf(false) }

    LaunchedEffect(flipped) {
        if (isFlipped != flipped) {
            shouldAnimate = false
            isFlipped = flipped
        }
    }

    LaunchedEffect(pleasure) {
        if (pleasure != null) {
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
            if (finalValue == 180f && !flipped) {
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
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        if (rotation < 90f) {
            PleasureBackCard()
        } else {
            pleasure?.let {
                PleasureCardContent(
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    pleasure = it
                )
            }
        }
    }
}

private fun overshootEasing(t: Float, tension: Float = 3f) =
    (t - 1).let { it * it * ((tension + 1) * it + tension) + 1 }

@Composable
private fun PleasureBackCard() {
    val gradients = flipGradients()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradients.primary)
    ) {
        // Decorative pattern - Plus subtil
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon avec effet lumineux amélioré
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.25f),
                                Color.White.copy(alpha = 0.12f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.pleasure_card_back_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.pleasure_card_back_subtitle),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.92f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Animation dots - Plus élégants
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == 1) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                Color.White.copy(
                                    alpha = if (index == 1) 0.8f else 0.5f
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun PleasureCardContent(modifier: Modifier = Modifier, pleasure: Pleasure) {
    val gradients = flipGradients()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradients.card)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Category chip
            CategoryChip(category = pleasure.category)

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.60f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎉",
                        fontSize = 36.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = pleasure.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                lineHeight = 30.sp,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                text = pleasure.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                maxLines = 4
            )

            Spacer(Modifier.weight(1f))

            // Footer section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                // Progress dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == 2) 10.dp else 7.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == 2) {
                                        Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
                                            )
                                        )
                                    } else {
                                        Brush.radialGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                            )
                                        )
                                    }
                                )
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.pleasure_card_id, pleasure.id),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun NotFlippedPleasureCardPreview() {
    FlipTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PleasureCard(
                pleasure = previewDailyPleasure,
                durationRotation = 0,
                flipped = false,
                onCardFlipped = {},
                onClick = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun FlippedPleasureCardPreview() {
    FlipTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PleasureCard(
                pleasure = previewDailyPleasure,
                durationRotation = 0,
                flipped = true,
                onCardFlipped = {},
                onClick = {}
            )
        }
    }
}
