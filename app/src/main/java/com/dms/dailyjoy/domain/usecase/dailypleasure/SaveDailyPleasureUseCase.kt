package com.dms.dailyjoy.domain.usecase.dailypleasure

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.DailyPleasureRepository
import javax.inject.Inject

class SaveDailyPleasureUseCase @Inject constructor(
    private val dailyPleasureRepository: DailyPleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure) {
        dailyPleasureRepository.saveDailyPleasure(pleasure)
    }
}
