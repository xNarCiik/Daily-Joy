package com.dms.dailyjoy.domain.usecase.settings

import com.dms.dailyjoy.domain.repository.SettingsRepository
import javax.inject.Inject

class SetDailyReminderStateUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    suspend operator fun invoke(enabled: Boolean) = settingsRepository.setDailyReminderEnabled(enabled)
}
