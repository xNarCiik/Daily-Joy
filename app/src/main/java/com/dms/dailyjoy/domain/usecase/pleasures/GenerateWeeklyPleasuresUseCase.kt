package com.dms.dailyjoy.domain.usecase.pleasures

import com.dms.dailyjoy.data.database.entity.WeeklyPleasureEntity
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.domain.repository.WeeklyPleasureRepository
import com.dms.dailyjoy.ui.util.getCurrentWeekId
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GenerateWeeklyPleasuresUseCase @Inject constructor(
    private val getPleasuresUseCase: GetPleasuresUseCase,
    private val weeklyPleasureRepository: WeeklyPleasureRepository
) {
    suspend operator fun invoke(): List<Pleasure> {
        val allPleasures = getPleasuresUseCase().first()
        val smallPleasures = allPleasures.filter { it.type == PleasureType.SMALL && it.isEnabled }
        val bigPleasures = allPleasures.filter { it.type == PleasureType.BIG && it.isEnabled }

        if (smallPleasures.size < 6 || bigPleasures.isEmpty()) {
            return emptyList()
        }

        val selectedSmall = smallPleasures.shuffled().take(6)
        val selectedBig = bigPleasures.shuffled().first()

        val weeklyPleasures = (selectedSmall + selectedBig).shuffled()

        val weekId = getCurrentWeekId()
        val weeklyPleasureEntities = weeklyPleasures.mapIndexed { index, pleasure ->
            WeeklyPleasureEntity(weekId = weekId, dayOfWeek = index, pleasureId = pleasure.id)
        }

        weeklyPleasureRepository.saveWeeklyPleasures(weeklyPleasureEntities)

        return weeklyPleasures
    }
}