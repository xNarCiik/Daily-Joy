package com.dms.flip.data.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Type converters to allow Room to reference complex data types.
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}
