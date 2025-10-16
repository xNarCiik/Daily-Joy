package com.dms.dailyjoy.domain.repository

interface DailyMessageRepository {
    fun getDailyMessages(): List<String>
    fun getRandomDailyMessage(): String
}