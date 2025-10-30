package com.dms.flip.domain.usecase.history

import com.dms.flip.data.model.PleasureHistoryEntry
import com.dms.flip.data.model.getTodayDayIdentifier
import com.dms.flip.domain.repository.PleasureRepository
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
