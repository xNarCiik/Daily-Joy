package com.dms.dailyjoy.domain.usecase.dailypleasure

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomPleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    operator fun invoke(category: PleasureCategory): Flow<Pleasure> {
        return repository.getRandomPleasure(category = category)
    }
}
