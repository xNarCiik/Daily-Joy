package com.dms.dailyjoy.data.repository

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dms.dailyjoy.domain.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Application.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val context: Application
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME = stringPreferencesKey("theme")
        val DAILY_REMINDER_ENABLED = booleanPreferencesKey("daily_reminder_enabled")
        val REMINDER_TIME = stringPreferencesKey("reminder_time")
    }

    override val theme: Flow<Theme> = context.dataStore.data
        .map {
            Theme.valueOf(it[PreferencesKeys.THEME] ?: Theme.SYSTEM.name)
        }

    override suspend fun setTheme(theme: Theme) {
        context.dataStore.edit {
            it[PreferencesKeys.THEME] = theme.name
        }
    }

    override val dailyReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map {
            it[PreferencesKeys.DAILY_REMINDER_ENABLED] ?: true
        }

    override suspend fun setDailyReminderEnabled(enabled: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.DAILY_REMINDER_ENABLED] = enabled
        }
    }

    override val reminderTime: Flow<String> = context.dataStore.data
        .map {
            it[PreferencesKeys.REMINDER_TIME] ?: "08:00"
        }

    override suspend fun setReminderTime(time: String) {
        context.dataStore.edit {
            it[PreferencesKeys.REMINDER_TIME] = time
        }
    }
}
