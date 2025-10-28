package com.dms.flip.domain.repository

interface DailyMessageRepository {
    fun getDailyMessages(): List<String>
    fun getRandomDailyMessage(): String
}