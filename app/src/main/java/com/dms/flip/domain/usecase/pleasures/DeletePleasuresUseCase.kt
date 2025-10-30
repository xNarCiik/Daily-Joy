package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class DeletePleasuresUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(pleasuresId: List<String>) {
        pleasureRepository.delete(pleasuresId)
    }
}
