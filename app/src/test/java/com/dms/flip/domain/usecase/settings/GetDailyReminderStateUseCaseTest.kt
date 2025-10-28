package com.dms.flip.domain.usecase.settings

import com.dms.flip.domain.repository.SettingsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetDailyReminderStateUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var getDailyReminderStateUseCase: GetDailyReminderStateUseCase

    @Before
    fun setUp() {
        settingsRepository = mock()
        getDailyReminderStateUseCase = GetDailyReminderStateUseCase(settingsRepository)
    }

    @Test
    fun `invoke should return daily reminder state from repository`() = runTest {
        // Given
        val enabled = true
        whenever(settingsRepository.dailyReminderEnabled).thenReturn(flowOf(enabled))

        // When
        val result = getDailyReminderStateUseCase().first()

        // Then
        assertThat(result).isEqualTo(enabled)
        verify(settingsRepository).dailyReminderEnabled
    }

    @Test
    fun `invoke should propagate exception from repository`() = runTest {
        // Given
        val exception = RuntimeException("Settings Error")
        whenever(settingsRepository.dailyReminderEnabled).thenReturn(flow { throw exception })

        // When & Then
        try {
            getDailyReminderStateUseCase().first()
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
        verify(settingsRepository).dailyReminderEnabled
    }
}
