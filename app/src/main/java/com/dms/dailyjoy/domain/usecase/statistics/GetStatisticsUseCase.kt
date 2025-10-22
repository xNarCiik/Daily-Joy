package com.dms.dailyjoy.domain.usecase.statistics

import com.dms.dailyjoy.domain.repository.StatisticsRepository
import com.dms.dailyjoy.ui.settings.statistics.StatisticsUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStatisticsUseCase @Inject constructor(
    private val statisticsRepository: StatisticsRepository
) {
    operator fun invoke(): Flow<StatisticsUiState> = statisticsRepository.getStatistics()
}
