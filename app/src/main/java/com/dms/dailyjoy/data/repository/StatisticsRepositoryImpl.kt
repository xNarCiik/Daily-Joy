
package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.domain.repository.StatisticsRepository
import com.dms.dailyjoy.ui.settings.statistics.CategoryStat
import com.dms.dailyjoy.ui.settings.statistics.DetailedStats
import com.dms.dailyjoy.ui.settings.statistics.MonthlyProgress
import com.dms.dailyjoy.ui.settings.statistics.StatisticsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor() : StatisticsRepository {
    override fun getStatistics(): Flow<StatisticsUiState> = flow {
        delay(1500) // Simulate network delay
        val stats = StatisticsUiState(
            isLoading = false,
            totalPleasures = 142,
            currentStreak = 12,
            averagePerDay = 4.2f,
            activeDays = 89,
            monthlyProgress = MonthlyProgress(completed = 21, total = 31),
            favoriteCategories = listOf(
                CategoryStat(name = "🍽️ Gastronomie", count = 45),
                CategoryStat(name = "🎵 Musique", count = 38),
                CategoryStat(name = "🏃 Sport", count = 32)
            ),
            detailedStats = DetailedStats(
                bestStreak = 18,
                weekProgress = "5 / 7 jours",
                weeklyAverage = "28 plaisirs",
                lastPleasure = "Il y a 2h"
            )
        )
        emit(stats)
    }
}
