package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.domain.model.Theme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val theme: Flow<Theme>
    suspend fun setTheme(theme: Theme)
}
