package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.local.LocalDailyMessagesDataSource
import com.dms.dailyjoy.domain.repository.DailyMessageRepository
import javax.inject.Inject

class DailyMessageRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDailyMessagesDataSource
) : DailyMessageRepository {
    override fun getDailyMessages(): List<String> = localDataSource.getDailyMessages()

    override fun getRandomDailyMessage(): String = getDailyMessages().random()
}
