package com.dms.dailyjoy.domain.usecase.weekly

import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.model.WeeklyPleasureDetails
import com.dms.dailyjoy.ui.weekly.PleasureStatus
import com.dms.dailyjoy.ui.weekly.WeeklyPleasureItem
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class BuildWeeklyPleasureItemsUseCaseTest {

    private lateinit var buildWeeklyPleasureItemsUseCase: BuildWeeklyPleasureItemsUseCase

    @Before
    fun setUp() {
        buildWeeklyPleasureItemsUseCase = BuildWeeklyPleasureItemsUseCase()
    }

    @Test
    fun `invoke should return weekly pleasure items`() {
        // Given
        val details = listOf(
            WeeklyPleasureDetails(dayOfWeek = 0, completed = false, pleasure = Pleasure()),
            WeeklyPleasureDetails(dayOfWeek = 1, completed = false, pleasure = Pleasure(isFlipped = true)),
            WeeklyPleasureDetails(dayOfWeek = 2, completed = false, pleasure = Pleasure(isFlipped = true))
        )

        // When
        val result = buildWeeklyPleasureItemsUseCase(details)

        // Then
        val expected = listOf(
            WeeklyPleasureItem(dayNameRes = R.string.full_day_monday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_tuesday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_wednesday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_thursday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_friday, pleasure = null, status = PleasureStatus.PAST_NOT_COMPLETED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_saturday, pleasure = null, status = PleasureStatus.LOCKED),
            WeeklyPleasureItem(dayNameRes = R.string.full_day_sunday, pleasure = null, status = PleasureStatus.LOCKED)
        )

        // TODO Note: This test is dependent on the current day.
        // A more robust test would mock the date.
        assertThat(result.map { it.copy(pleasure = null) }).isEqualTo(expected.map { it.copy(pleasure = null) })
    }

    @Test
    fun `invoke should return empty list for empty details`() {
        // When
        val result = buildWeeklyPleasureItemsUseCase(emptyList())

        // Then
        assertThat(result).hasSize(7)
        assertThat(result.all { it.pleasure == null }).isTrue()
    }
}
