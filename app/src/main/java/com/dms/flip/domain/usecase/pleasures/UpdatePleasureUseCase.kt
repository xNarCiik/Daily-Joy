package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.data.model.Pleasure
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class UpdatePleasureUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure) = pleasureRepository.update(pleasure)
}
