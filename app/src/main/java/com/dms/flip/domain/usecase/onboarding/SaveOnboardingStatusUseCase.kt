package com.dms.flip.domain.usecase.onboarding

import com.dms.flip.domain.repository.onboarding.OnboardingRepository
import javax.inject.Inject

class SaveOnboardingStatusUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(username: String) =
        onboardingRepository.saveOnboardingStatus(username = username)
}
