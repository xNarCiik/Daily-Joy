package com.dms.dailyjoy.ui.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccessTime
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.dms.dailyjoy.R
import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.ui.settings.dialog.ThemeDialog
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    theme: Theme,
    dailyReminderEnabled: Boolean,
    reminderTime: String,
    onThemeChanged: (Theme) -> Unit,
    onDailyReminderEnabledChanged: (Boolean) -> Unit,
    onReminderTimeChanged: (String) -> Unit,
    onNavigateToManagePleasures: () -> Unit
) {
    val context = LocalContext.current
    var showThemeDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onDailyReminderEnabledChanged(isGranted)
        }
    )

    if (showThemeDialog) {
        ThemeDialog(
            currentTheme = theme,
            onThemeSelected = { newTheme ->
                onThemeChanged(newTheme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showTimePicker) {
        ShowTimePicker(context, reminderTime, onReminderTimeChanged) {
            showTimePicker = false
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            SettingsSectionTitle(title = stringResource(id = R.string.settings_notifications_title))
        }
        item {
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = stringResource(id = R.string.settings_daily_reminder_title),
                subtitle = stringResource(id = R.string.settings_daily_reminder_subtitle),
                checked = dailyReminderEnabled,
                onCheckedChange = {
                    if (it && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onDailyReminderEnabledChanged(it)
                    }
                }
            )
        }
        item {
            AnimatedVisibility(visible = dailyReminderEnabled) {
                SettingsClickableItem(
                    icon = Icons.Default.AccessTime,
                    title = stringResource(id = R.string.settings_reminder_time_title),
                    subtitle = reminderTime,
                    onClick = { showTimePicker = true }
                )
            }
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
                onClick = onNavigateToManagePleasures
            )
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
        item {
            val themeSubtitle = when (theme) {
                Theme.LIGHT -> stringResource(R.string.settings_theme_light)
                Theme.DARK -> stringResource(R.string.settings_theme_dark)
                Theme.SYSTEM -> stringResource(R.string.settings_theme_system)
            }
            SettingsClickableItem(
                icon = Icons.Default.Palette,
                title = stringResource(id = R.string.settings_appearance_title),
                subtitle = stringResource(
                    id = R.string.settings_appearance_subtitle,
                    themeSubtitle
                ),
                onClick = { showThemeDialog = true }
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
                onClick = { rateApp(context) },
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
                onClick = { shareApp(context) },
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
                onClick = {
                    // TODO: Replace with privacy policy URL
                    val intent =
                        Intent(Intent.ACTION_VIEW, "https://privacy-policy-url.com".toUri())
                    context.startActivity(intent)
                },
                showChevron = false
            )
        }
    }
}

@Composable
private fun ShowTimePicker(
    context: Context,
    currentTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timeParts = currentTime.split(":")
    val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 8
    val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

    TimePickerDialog(
        context,
        { _, newHour, newMinute ->
            val newTime = String.format("%02d:%02d", newHour, newMinute)
            onTimeSelected(newTime)
        },
        hour,
        minute,
        true
    ).apply {
        setOnDismissListener { onDismiss() }
        show()
    }
}

private fun rateApp(context: Context) {
    val packageName = context.packageName
    try {
        val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=$packageName".toUri()
        )
        context.startActivity(intent)
    }
}

private fun shareApp(context: Context) {
    val packageName = context.packageName
    val shareText = context.getString(R.string.settings_share_text, packageName)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.settings_share_app)
        )
    )
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
            SettingsScreen(
                theme = Theme.SYSTEM,
                dailyReminderEnabled = true,
                reminderTime = "08:00",
                onThemeChanged = {},
                onDailyReminderEnabledChanged = {},
                onReminderTimeChanged = {},
                onNavigateToManagePleasures = {}
            )
        }
    }
}
