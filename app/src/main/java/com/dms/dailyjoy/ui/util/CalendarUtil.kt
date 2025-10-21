package com.dms.dailyjoy.ui.util

import java.util.Calendar

fun getCurrentWeekId(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.WEEK_OF_YEAR)
}
