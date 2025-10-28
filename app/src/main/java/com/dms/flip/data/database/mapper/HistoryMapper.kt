package com.dms.flip.data.database.mapper

import com.dms.flip.data.database.entity.PleasureHistoryEntry
import com.dms.flip.data.database.entity.getTodayDayIdentifier
import com.dms.flip.data.model.Pleasure

fun Pleasure.toHistoryEntry() = PleasureHistoryEntry(
    dayIdentifier = getTodayDayIdentifier(),
    dateDrawn = System.currentTimeMillis(),
    pleasureTitle = title,
    pleasureDescription = description,
    category = category
)

fun PleasureHistoryEntry.toPleasure() =
    Pleasure(title = pleasureTitle, description = pleasureDescription, category = category)
