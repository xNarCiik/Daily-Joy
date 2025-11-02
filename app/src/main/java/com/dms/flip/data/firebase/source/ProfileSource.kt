package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.PublicProfileDto
import com.dms.flip.data.firebase.dto.RecentActivityDto

interface ProfileSource {
    suspend fun getPublicProfile(userId: String): PublicProfileDto?
    suspend fun getRecentActivities(userId: String, limit: Int = 10): List<Pair<String, RecentActivityDto>>
}
