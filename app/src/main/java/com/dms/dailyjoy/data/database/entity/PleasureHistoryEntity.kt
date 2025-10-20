package com.dms.dailyjoy.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import java.time.LocalDate

/**
 * Represents a pleasure that has been selected for a specific day in the user's history.
 */
@Entity(tableName = "pleasure_history")
data class PleasureHistoryEntity(
    @PrimaryKey
    val date: LocalDate,
    val pleasureId: Int,
    val title: String,
    val description: String,
    val type: PleasureType,
    val category: PleasureCategory?,
    val isFlipped: Boolean,
    val isDone: Boolean
)
