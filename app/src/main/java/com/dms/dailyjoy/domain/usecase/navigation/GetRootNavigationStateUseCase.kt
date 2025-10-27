package com.dms.dailyjoy.domain.usecase.navigation

import com.dms.dailyjoy.data.repository.AuthRepository
import com.dms.dailyjoy.domain.model.RootNavigationState
import com.dms.dailyjoy.domain.repository.onboarding.OnboardingRepository
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
