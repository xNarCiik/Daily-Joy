package com.dms.flip.domain.repository.onboarding

import com.dms.flip.data.model.Pleasure
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun getOnboardingStatus(userId: String): Flow<Boolean>
    suspend fun saveOnboardingStatus(username: String, pleasures: List<Pleasure>)
}
