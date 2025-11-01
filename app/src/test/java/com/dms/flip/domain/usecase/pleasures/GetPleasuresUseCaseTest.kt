
package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.repository.PleasureRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetPleasuresUseCaseTest {

    private lateinit var repository: PleasureRepository
    private lateinit var getPleasuresUseCase: GetPleasuresUseCase

    @Before
    fun setUp() {
        repository = mock()
        getPleasuresUseCase = GetPleasuresUseCase(repository)
    }

    @Test
    fun `invoke should return pleasures from repository`() = runTest {
        // Given
        val pleasures = listOf(
            Pleasure(id = "1", title = "Lire un livre"),
            Pleasure(id = "2", title = "Écouter de la musique")
        )
        whenever(repository.getPleasures()).thenReturn(flowOf(pleasures))

        // When
        val result = getPleasuresUseCase().first()

        // Then
        assertThat(result).isEqualTo(pleasures)
    }

    @Test
    fun `invoke should return empty list when repository is empty`() = runTest {
        // Given
        whenever(repository.getPleasures()).thenReturn(flowOf(emptyList()))

        // When
        val result = getPleasuresUseCase().first()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `invoke should propagate exception from repository`() = runTest {
        // Given
        val exception = RuntimeException("DB Error")
        whenever(repository.getPleasures()).thenReturn(flow { throw exception })

        // When & Then
        try {
            getPleasuresUseCase().first()
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
    }
}
