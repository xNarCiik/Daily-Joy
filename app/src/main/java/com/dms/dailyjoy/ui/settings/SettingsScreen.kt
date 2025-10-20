package com.dms.dailyjoy.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            SettingsSectionTitle(title = stringResource(id = R.string.settings_notifications_title))
        }
        item {
            var isChecked by remember { mutableStateOf(true) }
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = stringResource(id = R.string.settings_daily_reminder_title),
                subtitle = stringResource(id = R.string.settings_daily_reminder_subtitle),
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            SettingsSectionTitle(title = stringResource(id = R.string.settings_personalization_title))
        }
        item {
            SettingsClickableItem(
                icon = Icons.Default.Edit,
                title = stringResource(id = R.string.settings_manage_pleasures_title),
                subtitle = stringResource(id = R.string.settings_manage_pleasures_subtitle),
                onClick = {}
            )
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item {
            SettingsClickableItem(
                icon = Icons.Default.Palette,
                title = stringResource(id = R.string.settings_appearance_title),
                subtitle = stringResource(id = R.string.settings_appearance_subtitle),
                onClick = {}
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item {
            SettingsSectionTitle(title = stringResource(id = R.string.settings_about_title))
        }
        item {
            SettingsClickableItem(
                icon = Icons.Default.StarRate,
                title = stringResource(id = R.string.settings_rate_app),
                onClick = {},
                showChevron = false
            )
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item {
            SettingsClickableItem(
                icon = Icons.Default.Share,
                title = stringResource(id = R.string.settings_share_app),
                onClick = {},
                showChevron = false
            )
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item {
            SettingsClickableItem(
                icon = Icons.Default.Shield,
                title = stringResource(id = R.string.settings_privacy_policy),
                onClick = {},
                showChevron = false
            )
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    showChevron: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (showChevron) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}


@LightDarkPreview
@Composable
fun SettingsScreenPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreen()
        }
    }
}
