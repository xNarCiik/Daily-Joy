package com.dms.flip.domain.usecase.onboarding

import com.dms.flip.domain.model.Pleasure
import com.dms.flip.domain.repository.onboarding.OnboardingRepository
import com.dms.flip.domain.usecase.settings.SetDailyReminderStateUseCase
import com.dms.flip.domain.usecase.settings.SetReminderTimeUseCase
import javax.inject.Inject

class SaveOnboardingStatusUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val setDailyReminderStateUseCase: SetDailyReminderStateUseCase,
    private val setReminderTimeUseCase: SetReminderTimeUseCase
) {
    suspend operator fun invoke(
        username: String,
        pleasures: List<Pleasure>,
        notificationEnabled: Boolean,
        reminderTime: String
    ) {
        onboardingRepository.saveOnboardingStatus(username = username, pleasures = pleasures)
        setDailyReminderStateUseCase(notificationEnabled)
        setReminderTimeUseCase(reminderTime)
    }
}
