package com.dms.flip.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dms.flip.data.model.PleasureCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(
    tableName = "pleasure_history",
    indices = [Index(value = ["dayIdentifier"], unique = true)]
)
data class PleasureHistoryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayIdentifier: String,
    val dateDrawn: Long,
    val isCompleted: Boolean = false,
    val pleasureTitle: String,
    val pleasureDescription: String,
    val category: PleasureCategory
)

fun getTodayDayIdentifier(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
