package com.dms.dailyjoy.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@Composable
fun HistoryScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "History"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    DailyJoyTheme {
        HistoryScreen()
    }
}