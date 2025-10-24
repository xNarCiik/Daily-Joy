package com.dms.dailyjoy.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
        val DAILY_REMINDER_ENABLED = booleanPreferencesKey("daily_reminder_enabled")
        val REMINDER_TIME = stringPreferencesKey("reminder_time")
    }

    override val theme: Flow<Theme> = dataStore.data
        .map {
            Theme.valueOf(it[PreferencesKeys.THEME] ?: Theme.SYSTEM.name)
        }

    override suspend fun setTheme(theme: Theme) {
        dataStore.edit {
            it[PreferencesKeys.THEME] = theme.name
        }
    }

    override val dailyReminderEnabled: Flow<Boolean> = dataStore.data
        .map {
            it[PreferencesKeys.DAILY_REMINDER_ENABLED] ?: false
        }

    override suspend fun setDailyReminderEnabled(enabled: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.DAILY_REMINDER_ENABLED] = enabled
        }
    }

    override val reminderTime: Flow<String> = dataStore.data
        .map {
            it[PreferencesKeys.REMINDER_TIME] ?: "11:00"
        }

    override suspend fun setReminderTime(time: String) {
        dataStore.edit {
            it[PreferencesKeys.REMINDER_TIME] = time
        }
    }
}
