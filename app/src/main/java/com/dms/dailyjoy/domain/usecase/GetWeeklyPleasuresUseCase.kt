package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.WeeklyPleasureRepository
import com.dms.dailyjoy.ui.util.getCurrentWeekId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWeeklyPleasuresUseCase @Inject constructor(
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val generatedWeeklyPleasuresUseCase: GenerateWeeklyPleasuresUseCase,
    private val weeklyPleasureRepository: WeeklyPleasureRepository
) {
    operator fun invoke(): Flow<List<Pleasure>> {
        val weekId = getCurrentWeekId()
        return weeklyPleasureRepository.getWeeklyPleasures(weekId)
            .flatMapLatest { weeklyPleasures ->
                val pleasureIds = weeklyPleasures.map { it.pleasureId }
                getPleasuresUseCase().map { allPleasures ->
                    pleasureIds.mapNotNull { id -> allPleasures.find { it.id == id } }
                }
            }
    }
}
