package com.dms.dailyjoy.ui.util

import java.util.Calendar

fun getCurrentWeekId(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.WEEK_OF_YEAR)
}


fun getCurrentDayIndex(): Int {
    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    // In Calendar, Sunday is 1, Monday is 2, ..., Saturday is 7
    // We want Monday to be 0, ..., Sunday to be 6
    return if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - 2
}
