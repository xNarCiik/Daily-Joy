package com.dms.flip.data.repository

import com.dms.flip.data.local.LocalDailyMessagesDataSource
import com.dms.flip.domain.repository.DailyMessageRepository
import javax.inject.Inject

class DailyMessageRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDailyMessagesDataSource
) : DailyMessageRepository {
    override fun getDailyMessages(): List<String> = localDataSource.getDailyMessages()

    override fun getRandomDailyMessage(): String = getDailyMessages().random()
}
