package com.dms.flip.data.model

import com.dms.flip.domain.model.Pleasure

fun Pleasure.toHistoryEntry() = PleasureHistoryEntry(
    dayIdentifier = getTodayDayIdentifier(),
    dateDrawn = System.currentTimeMillis(),
    pleasureTitle = title,
    pleasureDescription = description,
    category = category
)

fun PleasureHistoryEntry.toPleasure() =
    Pleasure(
        id = "",
        title = pleasureTitle,
        description = pleasureDescription ?: "",
        category = category
    )
