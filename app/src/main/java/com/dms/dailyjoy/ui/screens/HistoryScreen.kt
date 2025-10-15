package com.dms.dailyjoy.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.Greeting
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@Composable
fun HistoryScreen() {
    Greeting(name = "History")
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    DailyJoyTheme {
        HistoryScreen()
    }
}