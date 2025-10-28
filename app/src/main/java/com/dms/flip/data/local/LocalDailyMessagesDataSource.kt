package com.dms.flip.data.local

import android.app.Application
import com.dms.flip.R
import javax.inject.Inject

class LocalDailyMessagesDataSource @Inject constructor(
    private val context: Application
) {
    fun getDailyMessages(): List<String> {
        return context.resources.getStringArray(R.array.daily_messages).toList()
    }
}
