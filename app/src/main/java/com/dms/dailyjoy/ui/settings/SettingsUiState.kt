package com.dms.dailyjoy.ui.settings

import com.dms.dailyjoy.domain.model.Theme

data class SettingsUiState(
    val theme: Theme = Theme.SYSTEM,
    val dailyReminderEnabled: Boolean = false,
    val reminderTime: String = "11:00"
)

sealed interface SettingsEvent {
    data class OnThemeChanged(val theme: Theme) : SettingsEvent
    data class OnDailyReminderEnabledChanged(val enabled: Boolean) : SettingsEvent
    data class OnReminderTimeChanged(val time: String) : SettingsEvent
    data object OnDisconnectClicked : SettingsEvent
}
