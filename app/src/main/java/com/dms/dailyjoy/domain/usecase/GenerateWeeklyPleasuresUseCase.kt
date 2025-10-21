package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.database.entity.WeeklyPleasureEntity
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.data.model.WeeklyPleasures
import com.dms.dailyjoy.domain.repository.WeeklyPleasureRepository
import com.dms.dailyjoy.ui.util.getCurrentWeekId
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GenerateWeeklyPleasuresUseCase @Inject constructor(
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val weeklyPleasureRepository: WeeklyPleasureRepository
) {
    suspend operator fun invoke(): WeeklyPleasures {
        val allPleasures = getPleasuresUseCase().first()
        val smallPleasures = allPleasures.filter { it.type == PleasureType.SMALL }
        val bigPleasures = allPleasures.filter { it.type == PleasureType.BIG }

        if (smallPleasures.size < 6 || bigPleasures.isEmpty()) {
            // TODO FIX THAT return // Not enough pleasures
        }

        val selectedSmall = smallPleasures.shuffled().take(6)
        val selectedBig = bigPleasures.shuffled().first()

        val weeklyPleasures = (selectedSmall + selectedBig).shuffled()

        val weekId = getCurrentWeekId()
        val weeklyPleasure = weeklyPleasures.mapIndexed { index, pleasure ->
            WeeklyPleasureEntity(weekId = weekId, dayOfWeek = index + 1, pleasureId = pleasure.id)
        }

        weeklyPleasureRepository.saveWeeklyPleasures(weeklyPleasure)

        return WeeklyPleasures(pleasures = weeklyPleasures)
    }
}
