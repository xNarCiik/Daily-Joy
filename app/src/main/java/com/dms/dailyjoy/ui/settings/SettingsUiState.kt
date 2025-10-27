package com.dms.dailyjoy.ui.settings

import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.domain.model.UserInfo

data class SettingsUiState(
    val userInfo: UserInfo? = null,
    val theme: Theme = Theme.SYSTEM,
    val dailyReminderEnabled: Boolean = false,
    val reminderTime: String = "11:00"
)

sealed interface SettingsEvent {
    data class OnThemeChanged(val theme: Theme) : SettingsEvent
    data class OnDailyReminderEnabledChanged(val enabled: Boolean) : SettingsEvent
    data class OnReminderTimeChanged(val time: String) : SettingsEvent
    data object OnSignOut : SettingsEvent
}