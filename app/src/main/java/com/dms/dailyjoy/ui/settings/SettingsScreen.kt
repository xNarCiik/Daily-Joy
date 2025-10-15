package com.dms.dailyjoy.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Settings"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    DailyJoyTheme {
        SettingsScreen()
    }
}