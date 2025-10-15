package com.dms.dailyjoy.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.Greeting
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@Composable
fun HomeScreen() {
    Greeting(name = "Home")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DailyJoyTheme {
        HomeScreen()
    }
}