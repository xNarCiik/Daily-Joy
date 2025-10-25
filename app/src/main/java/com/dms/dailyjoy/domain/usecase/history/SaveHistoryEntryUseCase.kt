package com.dms.dailyjoy.domain.usecase.history

import com.dms.dailyjoy.data.database.entity.getTodayDayIdentifier
import com.dms.dailyjoy.data.database.mapper.toHistoryEntry
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureRepository
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