package com.dms.flip.ui.component

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable

@Composable
fun TimePicker(
    context: Context,
    currentTime: String,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timeParts = currentTime.split(":")
    val hour = timeParts.getOrNull(0)?.toIntOrNull() ?: 8
    val minute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

    TimePickerDialog(
        context,
        { _, newHour, newMinute ->
            val newTime = String.format("%02d:%02d", newHour, newMinute)
            onTimeSelected(newTime)
            onDismiss
        },
        hour,
        minute,
        true
    ).apply {
        setOnDismissListener { onDismiss() }
        show()
    }
}