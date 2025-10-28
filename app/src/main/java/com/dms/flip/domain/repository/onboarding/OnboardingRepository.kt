package com.dms.flip.domain.repository.onboarding

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun getOnboardingStatus(userId: String): Flow<Boolean>
    suspend fun saveOnboardingStatus(username: String)
}
