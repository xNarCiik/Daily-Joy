package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.local.LocalDailyMessagesDataSource
import com.dms.dailyjoy.domain.repository.DailyMessageRepository

class DailyMessageRepositoryImpl : DailyMessageRepository {
    override fun getDailyMessages(): List<String> = LocalDailyMessagesDataSource.dailyMessages

    override fun getRandomDailyMessage(): String = getDailyMessages().random()
}