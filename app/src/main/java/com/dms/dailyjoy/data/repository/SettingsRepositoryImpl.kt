package com.dms.dailyjoy.data.repository

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dms.dailyjoy.domain.model.Theme
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
    }

    override val theme = context.dataStore.data
        .map {
            Theme.valueOf(it[PreferencesKeys.THEME] ?: Theme.SYSTEM.name)
        }

    override suspend fun setTheme(theme: Theme) {
        context.dataStore.edit {
            it[PreferencesKeys.THEME] = theme.name
        }
    }
}
