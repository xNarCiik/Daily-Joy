package com.dms.flip.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getTodayDayIdentifier(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
