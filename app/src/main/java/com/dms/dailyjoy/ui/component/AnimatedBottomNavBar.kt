package com.dms.dailyjoy.ui.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.dms.dailyjoy.ui.util.fadeInContentAnimationDuration
import com.dms.dailyjoy.ui.util.navigationAnimationDuration

@Composable
fun AnimatedBottomNavBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    visible: Boolean
) {
    val transition = updateTransition(targetState = visible, label = "bottomBarTransition")

    val offsetY by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = if(visible) fadeInContentAnimationDuration else navigationAnimationDuration,
                easing = LinearOutSlowInEasing
            )
        },
        label = "offsetYAnim"
    ) { isVisible ->
        if (isVisible) 0.dp else 80.dp // Start outside screen
    }

    val currentAlpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = if(visible) fadeInContentAnimationDuration else navigationAnimationDuration, easing = LinearOutSlowInEasing)
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
        if (currentAlpha != 0f) {
            BottomNavBar(navController = navController)
        }
    }
}