package com.dms.dailyjoy.domain.usecase.pleasures

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.ui.util.getCurrentDayIndex
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetPleasureForTodayUseCase @Inject constructor(
    private val getWeeklyPleasuresUseCase: GetWeeklyPleasuresUseCase
) {
    suspend operator fun invoke(): Pleasure? =
        getWeeklyPleasuresUseCase().first().getOrNull(getCurrentDayIndex())?.pleasure
}