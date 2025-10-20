package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
import javax.inject.Inject

class UpdatePleasureUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure) = pleasureRepository.update(pleasure)
}
