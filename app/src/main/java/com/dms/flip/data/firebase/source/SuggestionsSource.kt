package com.dms.flip.data.firebase.source

import com.dms.flip.data.firebase.dto.SuggestionDto
import kotlinx.coroutines.flow.Flow

interface SuggestionsSource {
    fun observeSuggestions(uid: String): Flow<List<Pair<String, SuggestionDto>>>
    suspend fun hideSuggestion(uid: String, userId: String)
}
