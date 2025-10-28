package com.dms.flip.ui.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dms.flip.R

@Composable
fun AppIcon(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme() // TODO EXPORT THEME

    Icon(
        modifier = modifier,
        imageVector = Icons.Default.Favorite,
        contentDescription = stringResource(id = R.string.login_daily_joy_logo),
        tint = if (isDarkTheme) Color(0xFFFF6B9D) else Color(0xFFFF6B9D)
    )
}
