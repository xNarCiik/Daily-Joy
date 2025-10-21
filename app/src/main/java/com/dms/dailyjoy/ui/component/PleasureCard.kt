package com.dms.dailyjoy.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.data.model.Pleasure
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

    // Effect to sync the card's state with the external pleasure object without animation
    LaunchedEffect(pleasure) {
        if (isFlipped != pleasure.isFlipped) {
            shouldAnimate = false // Ensure no animation for external changes
            isFlipped = pleasure.isFlipped
        }
    }

    // Animation Rotation
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = if (shouldAnimate) {
            // Animate only when requested (i.e., on click)
            tween(
                durationMillis = durationRotation,
                easing = { it * it * (3 - 2 * it) } // Custom easing for a nice bounce effect
            )
        } else {
            // Otherwise, snap instantly to the new state
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

    // Jump animation (scale + offset)
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
            .width(250.dp)
            .height(400.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density // Perspective 3D
                scaleX = jumpScale
                scaleY = jumpScale
                translationY = jumpOffset
            },
        shape = RoundedCornerShape(size = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = {
            // Prevent flipping if it's already flipped or an animation is in progress
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
            PleasureBackgroundCard()
        } else {
            // Do inverse rotation to avoid mirror render
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
private fun PleasureCardContent(modifier: Modifier = Modifier, pleasure: Pleasure) {
    Box(modifier = Modifier.fillMaxSize()) {
        PleasureBackgroundCard()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            pleasure.category?.let {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(size = 12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "# ${
                            pleasure.category.name.lowercase().replaceFirstChar { it.uppercase() }
                        }",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(Modifier.weight(1f))

            Text(
                text = pleasure.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = pleasure.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.weight(1f))
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
