package com.dms.flip.domain.usecase.history

import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.model.getTodayDayIdentifier
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class SaveHistoryEntryUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure, markAsCompleted: Boolean = false) {
        val dayIdentifier = getTodayDayIdentifier()
        val existingEntry = pleasureRepository.getPleasureHistory(dayIdentifier)

        val entryToUpsert = existingEntry?.copy(
            completed = markAsCompleted
        ) ?: pleasure.toPleasureHistory()

        pleasureRepository.upsertPleasureHistory(entryToUpsert)
    }
}