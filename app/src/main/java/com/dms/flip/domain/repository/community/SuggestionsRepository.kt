package com.dms.flip.domain.repository.community

import com.dms.flip.domain.model.community.FriendSuggestion
import kotlinx.coroutines.flow.Flow

interface SuggestionsRepository {
    fun observeSuggestions(): Flow<List<FriendSuggestion>>
    suspend fun hideSuggestion(userId: String)
}
