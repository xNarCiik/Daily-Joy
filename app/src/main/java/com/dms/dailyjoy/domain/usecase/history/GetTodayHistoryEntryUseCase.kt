package com.dms.dailyjoy.domain.usecase.history

import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import com.dms.dailyjoy.data.database.entity.getTodayDayIdentifier
import com.dms.dailyjoy.domain.repository.PleasureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTodayHistoryEntryUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    operator fun invoke(): Flow<PleasureHistoryEntry?> = flow {
        emit(pleasureRepository.getHistoryEntryForDay(getTodayDayIdentifier()))
    }
}
