package com.dms.flip.domain.usecase.pleasures

import com.dms.flip.data.model.Pleasure
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class SavePleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure) = repository.insert(pleasure)
}
