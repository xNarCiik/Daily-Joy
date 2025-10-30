package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class DeletePleasureUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure) {
        pleasureRepository.delete(pleasure)
    }
}
