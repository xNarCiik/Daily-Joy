package com.dms.dailyjoy.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.Greeting
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@Composable
fun SettingsScreen() {
    Greeting(name = "Settings")
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    DailyJoyTheme {
        SettingsScreen()
    }
}