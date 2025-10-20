package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureHistoryRepository
import java.time.LocalDate
import javax.inject.Inject

class GetPleasureForTodayUseCase @Inject constructor(
    private val repository: PleasureHistoryRepository,
    private val getRandomPleasureUseCase: GetRandomPleasureUseCase
) {
    suspend operator fun invoke(): Pleasure? = repository.getForDate(LocalDate.now()) ?: getRandomPleasureUseCase()
}
