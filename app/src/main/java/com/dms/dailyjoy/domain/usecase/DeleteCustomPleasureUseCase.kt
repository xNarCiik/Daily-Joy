package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
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
