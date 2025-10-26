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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.dms.dailyjoy.R
import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.ui.settings.component.SettingsClickableItem
import com.dms.dailyjoy.ui.settings.component.SettingsSectionTitle
import com.dms.dailyjoy.ui.settings.component.SettingsSwitchItem
import com.dms.dailyjoy.ui.settings.dialog.NotificationPermissionDialog
import com.dms.dailyjoy.ui.settings.dialog.ThemeDialog
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    onNavigateToManagePleasures: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    var showThemeDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showNotificationPermissionDialog by remember { mutableStateOf(false) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                showNotificationPermissionDialog = true
            }
            onEvent(SettingsEvent.OnDailyReminderEnabledChanged(isGranted))
        }
    )

    if (showThemeDialog) {
        ThemeDialog(
            currentTheme = uiState.theme,
            onThemeSelected = { newTheme ->
                onEvent(SettingsEvent.OnThemeChanged(newTheme))
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }

    if (showTimePicker) {
        ShowTimePicker(context, uiState.reminderTime, {
            onEvent(SettingsEvent.OnReminderTimeChanged(it))
        }) {
            showTimePicker = false
        }
    }

    if (showNotificationPermissionDialog) {
        NotificationPermissionDialog(
            onDismiss = { showNotificationPermissionDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
            UserHeader(
                userName = "Utilisateur", // TODO: Récupérer depuis uiState
                userEmail = "user@example.com", // TODO: Récupérer depuis uiState
                userId = "UID-123456" // TODO: Récupérer depuis uiState
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        item { SettingsSectionTitle(title = stringResource(id = R.string.settings_notifications_title)) }

        item {
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = stringResource(id = R.string.settings_daily_reminder_title),
                subtitle = stringResource(id = R.string.settings_daily_reminder_subtitle),
                checked = uiState.dailyReminderEnabled,
                onCheckedChange = {
                    if (it && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onEvent(SettingsEvent.OnDailyReminderEnabledChanged(it))
                    }
                }
            )
        }

        item {
            AnimatedVisibility(visible = uiState.dailyReminderEnabled) {
                SettingsClickableItem(
                    icon = Icons.Default.AccessTime,
                    title = stringResource(id = R.string.settings_reminder_time_title),
                    subtitle = uiState.reminderTime,
                    onClick = { showTimePicker = true }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { SettingsSectionTitle(title = stringResource(id = R.string.settings_personalization_title)) }

        item {
            SettingsClickableItem(
                icon = Icons.Default.BarChart,
                title = stringResource(R.string.settings_statistics_title),
                subtitle = stringResource(R.string.settings_statistics_subtitle),
                onClick = onNavigateToStatistics
            )
        }

        item { SettingsDivider() }

        item {
            SettingsClickableItem(
                icon = Icons.Default.Edit,
                title = stringResource(id = R.string.settings_manage_pleasures_title),
                subtitle = stringResource(id = R.string.settings_manage_pleasures_subtitle),
                onClick = onNavigateToManagePleasures
            )
        }

        item { SettingsDivider() }

        item {
            val themeSubtitle = when (uiState.theme) {
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

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { SettingsSectionTitle(title = stringResource(id = R.string.settings_about_title)) }

        item {
            SettingsClickableItem(
                icon = Icons.Default.StarRate,
                title = stringResource(id = R.string.settings_rate_app),
                onClick = { rateApp(context) },
                showChevron = false
            )
        }

        item { SettingsDivider() }

        item {
            SettingsClickableItem(
                icon = Icons.Default.Share,
                title = stringResource(id = R.string.settings_share_app),
                onClick = { shareApp(context) },
                showChevron = false
            )
        }

        item { SettingsDivider() }

        item {
            SettingsClickableItem(
                icon = Icons.Default.Shield,
                title = stringResource(id = R.string.settings_privacy_policy),
                onClick = {
                    val intent =
                        Intent(
                            Intent.ACTION_VIEW,
                            context.getString(R.string.privacy_policy_url).toUri()
                        )
                    context.startActivity(intent)
                },
                showChevron = false
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { SettingsSectionTitle(title = "Compte") }

        item {
            SettingsClickableItem(
                icon = Icons.Default.Logout,
                title = "Déconnexion",
                subtitle = "Se déconnecter de l'application",
                onClick = onLogout,
                showChevron = false
            )
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun UserHeader(
    userName: String,
    userEmail: String,
    userId: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // User Info
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = userId,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    )
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
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "market://details?id=$packageName".toUri()
            )
        )
    } catch (_: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
        )
    }
}

private fun shareApp(context: Context) {
    val packageName = context.packageName
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.settings_share_text, packageName))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@LightDarkPreview
@Composable
private fun SettingsScreenPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreen(
                uiState = SettingsUiState(),
                onEvent = {},
                onNavigateToManagePleasures = {},
                onNavigateToStatistics = {}
            )
        }
    }
}