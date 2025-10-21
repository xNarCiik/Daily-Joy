package com.dms.dailyjoy.domain.usecase.settings

import com.dms.dailyjoy.domain.repository.SettingsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SetDailyReminderStateUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var setDailyReminderStateUseCase: SetDailyReminderStateUseCase

    @Before
    fun setUp() {
        settingsRepository = mock()
        setDailyReminderStateUseCase = SetDailyReminderStateUseCase(settingsRepository)
    }

    @Test
    fun `invoke should call setDailyReminderEnabled on repository`() = runTest {
        // Given
        val enabled = true

        // When
        setDailyReminderStateUseCase(enabled)

        // Then
        verify(settingsRepository).setDailyReminderEnabled(enabled)
    }

    @Test
    fun `invoke should handle exception from repository`() = runTest {
        // Given
        val enabled = true
        val exception = RuntimeException("Settings Error")
        whenever(settingsRepository.setDailyReminderEnabled(enabled)).thenThrow(exception)

        // When & Then
        try {
            setDailyReminderStateUseCase(enabled)
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
        verify(settingsRepository).setDailyReminderEnabled(enabled)
    }
}
