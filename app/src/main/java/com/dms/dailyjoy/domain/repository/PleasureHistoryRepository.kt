package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.model.Pleasure
import java.time.LocalDate

interface PleasureHistoryRepository {
    suspend fun getForDate(date: LocalDate): Pleasure?
    suspend fun save(pleasure: Pleasure)
}
