package com.dms.flip.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PleasureHistoryEntry(
    val id: String = "",
    val dayIdentifier: String,
    val dateDrawn: Long,
    val isCompleted: Boolean = false,
    val pleasureTitle: String,
    val pleasureDescription: String? = null,
    val category: PleasureCategory
)

fun getTodayDayIdentifier(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
