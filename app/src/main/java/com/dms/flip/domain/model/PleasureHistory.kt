package com.dms.flip.domain.model

import com.dms.flip.data.model.PleasureCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PleasureHistory(
    val id: String = "",
    val dayIdentifier: String,
    val dateDrawn: Long,
    val completed: Boolean = false,
    val pleasureTitle: String,
    val pleasureDescription: String? = null,
    val category: PleasureCategory
) {
    fun toPleasure() =
        Pleasure(
            id = id,
            title = pleasureTitle,
            description = pleasureDescription ?: "",
            category = category
        )
}

fun getTodayDayIdentifier(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
