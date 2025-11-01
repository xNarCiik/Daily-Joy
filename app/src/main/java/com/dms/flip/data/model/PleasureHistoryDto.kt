package com.dms.flip.data.model

import androidx.annotation.Keep
import com.dms.flip.domain.model.PleasureHistory
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class PleasureHistoryDto(
    val id: String = "",
    val pleasureTitle: String? = null,
    val pleasureCategory: String? = null,
    val pleasureDescription: String? = null,
    val dateDrawn: Long? = null,
    val completedAt: Long? = null,
    val completed: Boolean = false
) {
    fun toDomain(): PleasureHistory {
        return PleasureHistory(
            id = id,
            pleasureTitle = pleasureTitle,
            pleasureCategory = pleasureCategory?.let { PleasureCategory.valueOf(pleasureCategory) },
            pleasureDescription = pleasureDescription,
            dateDrawn = dateDrawn ?: 0L,
            completedAt = completedAt,
            completed = completed
        )
    }
}

fun PleasureHistory.toDto(): PleasureHistoryDto {
    return PleasureHistoryDto(
        id = id,
        pleasureTitle = pleasureTitle,
        pleasureCategory = pleasureCategory?.name,
        pleasureDescription = pleasureDescription,
        dateDrawn = dateDrawn,
        completedAt = completedAt,
        completed = completed
    )
}
