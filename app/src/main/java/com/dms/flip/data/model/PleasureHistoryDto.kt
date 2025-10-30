package com.dms.flip.data.model

import com.dms.flip.domain.model.PleasureHistory

data class PleasureHistoryDto(
    val dayIdentifier: String = "",
    val dateDrawn: Long = 0,
    val completed: Boolean = false,
    val pleasureTitle: String = "",
    val pleasureDescription: String? = null,
    val category: String = ""
) {
    fun toDomain(id: String): PleasureHistory {
        return PleasureHistory(
            id = id,
            dayIdentifier = dayIdentifier,
            dateDrawn = dateDrawn,
            completed = completed,
            pleasureTitle = pleasureTitle,
            pleasureDescription = pleasureDescription,
            category = PleasureCategory.valueOf(category)
        )
    }
}

fun PleasureHistory.toDto(): PleasureHistoryDto {
    return PleasureHistoryDto(
        dayIdentifier = dayIdentifier,
        dateDrawn = dateDrawn,
        completed = completed,
        pleasureTitle = pleasureTitle,
        pleasureDescription = pleasureDescription,
        category = category.name
    )
}
