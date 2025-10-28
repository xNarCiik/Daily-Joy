package com.dms.flip.domain.usecase.navigation

import com.dms.flip.data.repository.AuthRepository
import com.dms.flip.domain.model.RootNavigationState
import com.dms.flip.domain.repository.onboarding.OnboardingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetRootNavigationStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke(): Flow<RootNavigationState> =
        authRepository.getAuthState().flatMapLatest { user ->
            if (user == null) {
                flowOf(RootNavigationState.NotAuthenticated)
            } else {
                onboardingRepository.getOnboardingStatus(user.uid).map { isCompleted ->
                    if (isCompleted) {
                        RootNavigationState.AuthenticatedAndOnboarded
                    } else {
                        RootNavigationState.AuthenticatedButNotOnboarded
                    }
                }
            }
        }
}
