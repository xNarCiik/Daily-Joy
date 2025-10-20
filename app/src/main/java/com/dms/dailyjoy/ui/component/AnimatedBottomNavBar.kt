package com.dms.dailyjoy.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun AnimatedBottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    durationAnimation: Int
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val transition = updateTransition(targetState = visible, label = "bottomBarTransition")

    val offsetY by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = durationAnimation,
                easing = FastOutSlowInEasing
            )
        },
        label = "offsetYAnim"
    ) { isVisible ->
        if (isVisible) 0.dp else 80.dp // Start outside screen
    }

    val currentAlpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = durationAnimation, easing = LinearOutSlowInEasing)
        },
        label = "alphaAnim"
    ) { isVisible ->
        if (isVisible) 1f else 0f
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = offsetY.toPx()
                alpha = currentAlpha
            }
    ) {
        BottomNavBar(navController = navController)
    }
}