package com.dms.flip.ui.weekly.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dms.flip.data.model.Pleasure
import com.dms.flip.ui.component.PleasureCard
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewDailyPleasure

@Composable
fun ModalPleasureCard(
    modifier: Modifier = Modifier,
    pleasure: Pleasure?,
    onDismiss: () -> Unit
) {
    var pleasureForAnimation by remember { mutableStateOf<Pleasure?>(null) }
    LaunchedEffect(pleasure) {
        if (pleasure != null) {
            pleasureForAnimation = pleasure
        }
    }

    val showPleasureCard = pleasure != null

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showPleasureCard,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
                    .clickable { onDismiss() }
            )
        }

        AnimatedVisibility(
            visible = showPleasureCard,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                pleasureForAnimation?.let {
                    PleasureCard(
                        modifier = Modifier
                            .offset(y = 40.dp)
                            .clickable(enabled = false) {},
                        pleasure = it,
                        flipped = true,
                        durationRotation = 0,
                        onCardFlipped = {},
                        onClick = {}
                    )
                }
            }
        }
    }
}

@LightDarkPreview
@Composable
private fun ModalPleasureCardPreview() {
    FlipTheme {
        ModalPleasureCard(pleasure = previewDailyPleasure) { }
    }
}
