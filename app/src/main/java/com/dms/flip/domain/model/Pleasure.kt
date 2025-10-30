package com.dms.flip.domain.model

import com.dms.flip.data.model.PleasureCategory

data class Pleasure(
    val id: String,
    val title: String = "",
    val description: String = "",
    val category: PleasureCategory = PleasureCategory.OTHER,
    val isEnabled: Boolean = true
) {
    fun toPleasureHistory() = PleasureHistory(
        dayIdentifier = getTodayDayIdentifier(),
        dateDrawn = System.currentTimeMillis(),
        pleasureTitle = title,
        pleasureDescription = description,
        category = category
    )
}