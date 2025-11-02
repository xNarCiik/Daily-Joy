package com.dms.flip.domain.usecase.community

import com.dms.flip.domain.model.community.FriendSuggestion
import com.dms.flip.domain.repository.community.SuggestionsRepository
import com.dms.flip.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSuggestionsUseCase @Inject constructor(
    private val suggestionsRepository: SuggestionsRepository
) {
    operator fun invoke(): Flow<List<FriendSuggestion>> = suggestionsRepository.observeSuggestions()
}

class HideSuggestionUseCase @Inject constructor(
    private val suggestionsRepository: SuggestionsRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> =
        runCatching { suggestionsRepository.hideSuggestion(userId) }
            .fold(
                onSuccess = { Result.Ok(Unit) },
                onFailure = { Result.Err(it) }
            )
}
