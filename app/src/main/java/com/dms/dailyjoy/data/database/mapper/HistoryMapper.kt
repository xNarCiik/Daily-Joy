package com.dms.dailyjoy.data.database.mapper

import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import com.dms.dailyjoy.data.model.Pleasure

fun Pleasure.toHistoryEntry() = PleasureHistoryEntry(
    dateDrawn = System.currentTimeMillis(),
    pleasureTitle = title,
    pleasureDescription = description,
    category = category
)
