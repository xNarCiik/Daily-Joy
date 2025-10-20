package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
import javax.inject.Inject

class SavePleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure) = repository.insert(pleasure)
}
