package com.dms.flip.domain.usecase.settings

import com.dms.flip.domain.repository.SettingsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SetReminderTimeUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var setReminderTimeUseCase: SetReminderTimeUseCase

    @Before
    fun setUp() {
        settingsRepository = mock()
        setReminderTimeUseCase = SetReminderTimeUseCase(settingsRepository)
    }

    @Test
    fun `invoke should call setReminderTime on repository`() = runTest {
        // Given
        val time = "10:00"

        // When
        setReminderTimeUseCase(time)

        // Then
        verify(settingsRepository).setReminderTime(time)
    }

    @Test
    fun `invoke should handle exception from repository`() = runTest {
        // Given
        val time = "10:00"
        val exception = RuntimeException("Settings Error")
        whenever(settingsRepository.setReminderTime(time)).thenThrow(exception)

        // When & Then
        try {
            setReminderTimeUseCase(time)
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
        verify(settingsRepository).setReminderTime(time)
    }
}
