package com.dms.flip.domain.usecase.weekly

import com.dms.flip.R
import com.dms.flip.ui.weekly.PleasureStatus
import com.dms.flip.ui.weekly.WeeklyPleasureItem
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class GetWeeklyPleasuresStatsUseCaseTest {

    private lateinit var getWeeklyPleasuresStatsUseCase: GetWeeklyPleasuresStatsUseCase

    @Before
    fun setUp() {
        getWeeklyPleasuresStatsUseCase = GetWeeklyPleasuresStatsUseCase()
    }

    @Test
    fun `invoke should return correct stats`() {
        // Given
        val weeklyItems = listOf(
            WeeklyPleasureItem(dayNameRes = R.string.full_day_monday, pleasure = null, status = PleasureStatus.PAST_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_tuesday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_wednesday, pleasure = null, status = PleasureStatus.CURRENT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_thursday, pleasure = null, status = PleasureStatus.LOCKED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_friday, pleasure = null, status = PleasureStatus.LOCKED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_saturday, pleasure = null, status = PleasureStatus.LOCKED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_sunday, pleasure = null, status = PleasureStatus.LOCKED)
        )

        // When
        val result = getWeeklyPleasuresStatsUseCase(weeklyItems)

        // Then
        assertThat(result.completed).isEqualTo(2)
        assertThat(result.remaining).isEqualTo(5)
    }

    @Test
    fun `invoke should return zero completed for no completed items`() {
        // Given
        val weeklyItems = listOf(
            WeeklyPleasureItem(dayNameRes = R.string.full_day_monday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_tuesday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED)
        )

        // When
        val result = getWeeklyPleasuresStatsUseCase(weeklyItems)

        // Then
        assertThat(result.completed).isEqualTo(0)
        assertThat(result.remaining).isEqualTo(7)
    }

    @Test
    fun `invoke should return correct stats for all completed`() {
        // Given
        val weeklyItems = listOf(
            WeeklyPleasureItem(dayNameRes = R.string.full_day_monday, pleasure = null, status = PleasureStatus.PAST_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_tuesday, pleasure = null, status = PleasureStatus.PAST_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_wednesday, pleasure = null, status = PleasureStatus.PAST_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_thursday, pleasure = null, status = PleasureStatus.PAST_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_friday, pleasure = null, status = PleasureStatus.PAST_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_saturday, pleasure = null, status = PleasureStatus.PAST_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_sunday, pleasure = null, status = PleasureStatus.PAST_COMPLETED)
        )

        // When
        val result = getWeeklyPleasuresStatsUseCase(weeklyItems)

        // Then
        assertThat(result.completed).isEqualTo(7)
        assertThat(result.remaining).isEqualTo(0)
    }

    @Test
    fun `invoke should return zero for empty list`() {
        // When
        val result = getWeeklyPleasuresStatsUseCase(emptyList())

        // Then
        assertThat(result.completed).isEqualTo(0)
        assertThat(result.remaining).isEqualTo(7)
    }
}
