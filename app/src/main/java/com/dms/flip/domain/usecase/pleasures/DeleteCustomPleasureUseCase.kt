package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.data.model.Pleasure
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class DeleteCustomPleasureUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure) {
        if (pleasure.isCustom) {
            pleasureRepository.delete(pleasure)
        }
    }
}
