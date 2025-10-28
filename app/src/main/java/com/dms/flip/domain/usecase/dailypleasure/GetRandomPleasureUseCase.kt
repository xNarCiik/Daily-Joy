package com.dms.flip.domain.usecase.dailypleasure

import com.dms.flip.data.model.Pleasure
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomPleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    operator fun invoke(category: PleasureCategory): Flow<Pleasure> {
        return repository.getRandomPleasure(category = category)
    }
}
