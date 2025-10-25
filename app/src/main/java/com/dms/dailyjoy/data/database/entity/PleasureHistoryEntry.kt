package com.dms.dailyjoy.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dms.dailyjoy.data.model.PleasureCategory

@Entity(tableName = "pleasure_history")
data class PleasureHistoryEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateDrawn: Long,
    val isCompleted: Boolean = false,
    val pleasureTitle: String,
    val pleasureDescription: String,
    val category: PleasureCategory
)