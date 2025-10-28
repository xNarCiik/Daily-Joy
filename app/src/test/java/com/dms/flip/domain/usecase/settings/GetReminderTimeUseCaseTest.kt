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

class GetReminderTimeUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var getReminderTimeUseCase: GetReminderTimeUseCase

    @Before
    fun setUp() {
        settingsRepository = mock()
        getReminderTimeUseCase = GetReminderTimeUseCase(settingsRepository)
    }

    @Test
    fun `invoke should return reminder time from repository`() = runTest {
        // Given
        val time = "10:00"
        whenever(settingsRepository.reminderTime).thenReturn(flowOf(time))

        // When
        val result = getReminderTimeUseCase().first()

        // Then
        assertThat(result).isEqualTo(time)
        verify(settingsRepository).reminderTime
    }

    @Test
    fun `invoke should propagate exception from repository`() = runTest {
        // Given
        val exception = RuntimeException("Settings Error")
        whenever(settingsRepository.reminderTime).thenReturn(flow { throw exception })

        // When & Then
        try {
            getReminderTimeUseCase().first()
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
        verify(settingsRepository).reminderTime
    }
}
