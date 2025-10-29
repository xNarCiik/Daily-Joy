package com.dms.flip.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.theme.flipGradients
import com.dms.flip.ui.util.LightDarkPreview

@Composable
fun FlipLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(140.dp)
            .background(
                brush = flipGradients().logo,
                shape = MaterialTheme.shapes.large
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "F",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-2).sp
            ),
            color = MaterialTheme.colorScheme.surface
        )
    }
}

@LightDarkPreview
@Composable
private fun FlipLogoPreview() {
    FlipTheme {
        FlipLogo()
    }
}
