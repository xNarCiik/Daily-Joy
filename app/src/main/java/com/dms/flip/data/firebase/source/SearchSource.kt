package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.PublicProfileDto

data class SearchResultDto(
    val id: String,
    val profile: PublicProfileDto
)

interface SearchSource {
    suspend fun searchUsers(query: String, limit: Int = 20): List<SearchResultDto>
}
