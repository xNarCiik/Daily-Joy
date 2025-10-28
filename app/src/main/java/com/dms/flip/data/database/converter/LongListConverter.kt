package com.dms.flip.data.database.converter

import androidx.room.TypeConverter

class LongListConverter {
    @TypeConverter
    fun fromString(value: String?): List<Long>? {
        return value?.split(",")?.mapNotNull { it.toLongOrNull() }
    }

    @TypeConverter
    fun fromList(list: List<Long>?): String? {
        return list?.joinToString(",")
    }
}