package com.dms.dailyjoy.ui.settings

import com.dms.dailyjoy.domain.model.Theme

data class SettingsUiState(
    val userInfo: UserInfo? = null,
    val theme: Theme = Theme.SYSTEM,
    val dailyReminderEnabled: Boolean = false,
    val reminderTime: String = "11:00"
)

data class UserInfo(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val avatarUrl: String? = null,
)

sealed interface SettingsEvent {
    data class OnThemeChanged(val theme: Theme) : SettingsEvent
    data class OnDailyReminderEnabledChanged(val enabled: Boolean) : SettingsEvent
    data class OnReminderTimeChanged(val time: String) : SettingsEvent
}
