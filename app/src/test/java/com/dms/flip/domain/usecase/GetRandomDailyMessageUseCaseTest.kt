package com.dms.flip.domain.usecase

import com.dms.flip.domain.repository.DailyMessageRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetRandomDailyMessageUseCaseTest {

    private lateinit var repository: DailyMessageRepository
    private lateinit var getRandomDailyMessageUseCase: GetRandomDailyMessageUseCase

    @Before
    fun setUp() {
        repository = mock()
        getRandomDailyMessageUseCase = GetRandomDailyMessageUseCase(repository)
    }

    @Test
    fun `invoke should return daily message from repository`() = runTest {
        // Given
        val message = "Carpe Diem"
        whenever(repository.getRandomDailyMessage()).thenReturn(message)

        // When
        val result = getRandomDailyMessageUseCase()

        // Then
        assertThat(result).isEqualTo(message)
        verify(repository).getRandomDailyMessage()
    }

    @Test
    fun `invoke should propagate exception from repository`() = runTest {
        // Given
        val exception = RuntimeException("DB Error")
        whenever(repository.getRandomDailyMessage()).thenThrow(exception)

        // When & Then
        try {
            getRandomDailyMessageUseCase()
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
        verify(repository).getRandomDailyMessage()
    }
}
