package com.dms.dailyjoy.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_pleasure")
data class WeeklyPleasureEntity(
    @PrimaryKey()
    val weekId: Int,
    val dayOfWeek: Int,
    val pleasureId: Int
)
