package com.dms.flip.domain.usecase.statistics

import com.dms.flip.domain.repository.StatisticsRepository
import com.dms.flip.ui.settings.statistics.StatisticsUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatisticsUseCase @Inject constructor(
    private val statisticsRepository: StatisticsRepository
) {
    operator fun invoke(): Flow<StatisticsUiState> = statisticsRepository.getStatistics()
}
