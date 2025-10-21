package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val theme: Flow<Theme>
    suspend fun setTheme(theme: Theme)

    val dailyReminderEnabled: Flow<Boolean>
    suspend fun setDailyReminderEnabled(enabled: Boolean)

    val reminderTime: Flow<String>
    suspend fun setReminderTime(time: String)
}