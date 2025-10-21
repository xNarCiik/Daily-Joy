package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReminderTimeUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Flow<String> = settingsRepository.reminderTime
}
