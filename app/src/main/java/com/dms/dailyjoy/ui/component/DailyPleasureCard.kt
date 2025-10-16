package com.dms.dailyjoy.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyPleasureCard(
    modifier: Modifier = Modifier,
    pleasure: Pleasure,
    flipCard: () -> Unit
) {
    // Animation Rotation
    val rotation by animateFloatAsState(
        targetValue = if (pleasure.isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 700),
        label = "rotationAnimation"
    )

    Card(
        modifier = modifier
            .width(250.dp)
            .height(400.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density // Perspective 3D
            },
        shape = RoundedCornerShape(size = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = { flipCard() }
    ) {
        if (rotation < 90f) {
            BackCardContent()
        } else {
            // Do inverse rotation to avoid mirror render
            DailyPleasureCardContent(
                modifier = Modifier.graphicsLayer { rotationY = 180f },
                pleasure = pleasure
            )
        }
    }
}

@Composable
private fun BackCardContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(all = 24.dp)
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.Default.QuestionMark,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
    }
}

@Composable
private fun DailyPleasureCardContent(modifier: Modifier = Modifier, pleasure: Pleasure) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Spacer(Modifier.weight(1f))

        Text(
            text = pleasure.title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = pleasure.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = 12.dp),
            onClick = { }
        ) {
            Text(
                text = "Plaisir réalisé ?",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotFlippedDailyPleasureCardPreview() {
    DailyJoyTheme {
        DailyPleasureCard(
            pleasure = Pleasure(
                id = 0,
                title = "Pleasure Title",
                description = "Pleasure Description",
                type = PleasureType.BIG,
                category = PleasureCategory.CREATIVE
            ),
            flipCard = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FlippedDailyPleasureCardPreview() {
    DailyJoyTheme {
        DailyPleasureCard(
            pleasure = Pleasure(
                id = 0,
                title = "Pleasure Title",
                description = "Pleasure Description",
                type = PleasureType.BIG,
                category = PleasureCategory.CREATIVE,
                isFlipped = true
            ),
            flipCard = {}
        )
    }
}