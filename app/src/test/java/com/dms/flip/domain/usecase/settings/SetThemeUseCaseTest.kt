package com.dms.flip.domain.usecase.settings

import com.dms.flip.domain.model.Theme
import com.dms.flip.domain.repository.SettingsRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SetThemeUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var setThemeUseCase: SetThemeUseCase

    @Before
    fun setUp() {
        settingsRepository = mock()
        setThemeUseCase = SetThemeUseCase(settingsRepository)
    }

    @Test
    fun `invoke should call setTheme on repository`() = runTest {
        // Given
        val theme = Theme.DARK

        // When
        setThemeUseCase(theme)

        // Then
        verify(settingsRepository).setTheme(theme)
    }

    @Test
    fun `invoke should handle exception from repository`() = runTest {
        // Given
        val theme = Theme.LIGHT
        val exception = RuntimeException("Settings Error")
        whenever(settingsRepository.setTheme(theme)).thenThrow(exception)

        // When & Then
        try {
            setThemeUseCase(theme)
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
        verify(settingsRepository).setTheme(theme)
    }
}
