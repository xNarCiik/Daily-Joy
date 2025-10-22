package com.dms.dailyjoy.ui.settings.statistics

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val totalPleasures: Int = 0,
    val currentStreak: Int = 0,
    val averagePerDay: Float = 0f,
    val activeDays: Int = 0,
    val monthlyProgress: MonthlyProgress = MonthlyProgress(0, 0),
    val favoriteCategories: List<CategoryStat> = emptyList(),
    val detailedStats: DetailedStats = DetailedStats(0, "", "", ""),
    val error: Int? = null
)

data class MonthlyProgress(val completed: Int, val total: Int)

data class CategoryStat(val name: String, val count: Int)

data class DetailedStats(
    val bestStreak: Int,
    val weekProgress: String,
    val weeklyAverage: String,
    val lastPleasure: String
)

sealed interface StatisticsEvent {
    data object OnRetryClicked : StatisticsEvent
}
