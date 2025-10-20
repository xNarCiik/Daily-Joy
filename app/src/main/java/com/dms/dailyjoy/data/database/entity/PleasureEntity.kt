package com.dms.dailyjoy.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType

@Entity(tableName = "pleasure")
data class PleasureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val type: PleasureType,
    val category: PleasureCategory?,
    val isFlipped: Boolean = false,
    val isDone: Boolean = false,
    val isCustom: Boolean = false,
    val isEnabled: Boolean = true
)
