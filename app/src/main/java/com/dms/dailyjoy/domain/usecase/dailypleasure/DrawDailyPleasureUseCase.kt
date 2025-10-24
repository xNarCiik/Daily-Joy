package com.dms.dailyjoy.domain.usecase.dailypleasure

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DrawDailyPleasureUseCase @Inject constructor(
    private val repository: PleasureRepository
) {
    operator fun invoke(category: PleasureCategory): Flow<Pleasure> {
        return repository.getAllPleasures().map { pleasuresList ->
            val randomPleasure =
                pleasuresList.filter { pleasure -> pleasure.category == category }.randomOrNull()
            randomPleasure ?: pleasuresList.random()
        }
    }
}
