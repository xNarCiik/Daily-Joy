package com.dms.dailyjoy.ui.settings.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth(),
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@LightDarkPreview
@Composable
private fun SettingsSectionTitlePreview() {
    DailyJoyTheme {
        SettingsSectionTitle(title = "Notifications")
    }
}
