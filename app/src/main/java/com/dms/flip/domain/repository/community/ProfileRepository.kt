package com.dms.flip.domain.repository.community

import com.dms.flip.domain.model.community.PublicProfile

interface ProfileRepository {
    suspend fun getPublicProfile(userId: String): PublicProfile
}
