package com.dms.flip.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dms.flip.data.model.PleasureCategory

@Entity(tableName = "pleasure")
data class PleasureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: PleasureCategory,
    val isDone: Boolean = false,
    val isEnabled: Boolean = true
)
