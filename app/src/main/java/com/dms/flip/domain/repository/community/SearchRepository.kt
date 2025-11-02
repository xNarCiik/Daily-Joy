package com.dms.flip.domain.repository.community

import com.dms.flip.domain.model.community.UserSearchResult

interface SearchRepository {
    suspend fun searchUsers(query: String, limit: Int = 20): List<UserSearchResult>
}
