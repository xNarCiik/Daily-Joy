package com.dms.flip.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    // TODO EXPORT STRING
    return when {
        diff < 60_000 -> "à l'instant"
        diff < 3_600_000 -> "${diff / 60_000}min"
        diff < 86_400_000 -> "${diff / 3_600_000}h"
        else -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(timestamp))
    }
}
