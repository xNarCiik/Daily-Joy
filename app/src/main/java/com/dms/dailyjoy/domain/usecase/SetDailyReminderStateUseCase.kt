package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.repository.SettingsRepository
import javax.inject.Inject

class SetDailyReminderStateUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(enabled: Boolean) = settingsRepository.setDailyReminderEnabled(enabled)
}
