package com.dms.dailyjoy.domain.usecase.onboarding

import com.dms.dailyjoy.domain.repository.onboarding.OnboardingRepository
import javax.inject.Inject

class SaveOnboardingStatusUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(username: String) =
        onboardingRepository.saveOnboardingStatus(username = username)
}
