package com.dms.dailyjoy.data.database.entity

import androidx.room.Entity

@Entity(tableName = "weekly_pleasure", primaryKeys = ["weekId", "dayOfWeek"])
data class WeeklyPleasureEntity(
    val weekId: Int,
    val dayOfWeek: Int,
    val pleasureId: Int
)
