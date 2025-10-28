package com.dms.flip.domain.usecase.settings

import com.dms.flip.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyReminderStateUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = settingsRepository.dailyReminderEnabled
}
