package com.dms.dailyjoy.domain.usecase.pleasures

import com.dms.dailyjoy.domain.model.WeeklyPleasureDetails
import com.dms.dailyjoy.domain.repository.WeeklyPleasureRepository
import com.dms.dailyjoy.ui.util.getCurrentWeekId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWeeklyPleasuresUseCase @Inject constructor(
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val weeklyPleasureRepository: WeeklyPleasureRepository
) {
    operator fun invoke(): Flow<List<WeeklyPleasureDetails>> {
        val weekId = getCurrentWeekId()
        return weeklyPleasureRepository.getWeeklyPleasures(weekId)
            .flatMapLatest { weeklyPleasuresEntities ->
                getPleasuresUseCase().map { allPleasures ->
                    weeklyPleasuresEntities.mapNotNull { entity ->
                        allPleasures.find { it.id == entity.pleasureId }?.let { pleasure ->
                            WeeklyPleasureDetails(
                                pleasure = pleasure,
                                dayOfWeek = entity.dayOfWeek,
                                completed = pleasure.isDone
                            )
                        }
                    }
                }
            }
    }
}
