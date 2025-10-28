
package com.dms.flip.domain.repository

import com.dms.flip.ui.settings.statistics.StatisticsUiState
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getStatistics(): Flow<StatisticsUiState>
}
