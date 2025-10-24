package com.dms.dailyjoy.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.DailyPleasureRepository
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DailyPleasureRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val pleasureRepository: PleasureRepository
) : DailyPleasureRepository {

    private object PreferencesKeys {
        val DRAWN_PLEASURE_ID = stringPreferencesKey("drawn_pleasure_id")
        val DRAWN_PLEASURE_DATE = stringPreferencesKey("drawn_pleasure_date")
    }

    override fun getDailyPleasure(): Flow<Pleasure?> {
        return dataStore.data.map { preferences ->
            val savedDate = preferences[PreferencesKeys.DRAWN_PLEASURE_DATE]
            val savedId = preferences[PreferencesKeys.DRAWN_PLEASURE_ID]

            if (savedDate == LocalDate.now().toString() && savedId != null) {
                pleasureRepository.getAllPleasures().first().find { it.id.toString() == savedId }
            } else {
                null
            }
        }
    }

    override suspend fun saveDailyPleasure(pleasure: Pleasure) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DRAWN_PLEASURE_ID] = pleasure.id.toString()
            preferences[PreferencesKeys.DRAWN_PLEASURE_DATE] = LocalDate.now().toString()
        }
    }
}
