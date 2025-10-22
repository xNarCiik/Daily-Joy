
package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.ui.settings.statistics.StatisticsUiState
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getStatistics(): Flow<StatisticsUiState>
}
