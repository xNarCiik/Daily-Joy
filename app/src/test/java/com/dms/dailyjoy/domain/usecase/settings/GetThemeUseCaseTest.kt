package com.dms.dailyjoy.domain.usecase.settings

import com.dms.dailyjoy.domain.model.Theme
import com.dms.dailyjoy.domain.repository.SettingsRepository
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

class GetThemeUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var getThemeUseCase: GetThemeUseCase

    @Before
    fun setUp() {
        settingsRepository = mock()
        getThemeUseCase = GetThemeUseCase(settingsRepository)
    }

    @Test
    fun `invoke should return theme from repository`() = runTest {
        // Given
        val theme = Theme.DARK
        whenever(settingsRepository.theme).thenReturn(flowOf(theme))

        // When
        val result = getThemeUseCase().first()

        // Then
        assertThat(result).isEqualTo(theme)
        verify(settingsRepository).theme
    }

    @Test
    fun `invoke should propagate exception from repository`() = runTest {
        // Given
        val exception = RuntimeException("Settings Error")
        whenever(settingsRepository.theme).thenReturn(flow { throw exception })

        // When & Then
        try {
            getThemeUseCase().first()
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
        verify(settingsRepository).theme
    }
}
