package com.dms.flip.domain.usecase.history

import com.dms.flip.data.database.entity.getTodayDayIdentifier
import com.dms.flip.data.database.mapper.toHistoryEntry
import com.dms.flip.data.model.Pleasure
import com.dms.flip.domain.repository.PleasureRepository
import javax.inject.Inject

class SaveHistoryEntryUseCase @Inject constructor(
    private val pleasureRepository: PleasureRepository
) {
    suspend operator fun invoke(pleasure: Pleasure, markAsCompleted: Boolean = false) {
        val dayIdentifier = getTodayDayIdentifier()
        val existingEntry = pleasureRepository.getHistoryEntryForDay(dayIdentifier)

        val entryToUpsert = existingEntry?.copy(
            isCompleted = markAsCompleted
        ) ?: pleasure.toHistoryEntry()

        pleasureRepository.upsertHistoryEntry(entryToUpsert)
    }
}