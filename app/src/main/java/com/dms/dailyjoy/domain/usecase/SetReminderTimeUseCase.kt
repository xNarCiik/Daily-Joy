package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.repository.SettingsRepository
import javax.inject.Inject

class SetReminderTimeUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(time: String) = settingsRepository.setReminderTime(time)
}
